package com.numetriclabz.sendrequests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WifiStatusReceiver extends BroadcastReceiver {
	public WifiStatusReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
			if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
				//do stuff
				new PostClass(context).execute();
			} else {
				// wifi connection was lost
			}
		}
	}
	private class PostClass extends AsyncTask<String, Void, Void> {

		private final Context context;
		private ProgressDialog progress;

		public PostClass(Context c){

			this.context = c;

		}

		protected void onPreExecute(){
			progress= new ProgressDialog(this.context);
			progress.setMessage("Loading");
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			try {

				URL url = new URL("https://api.particle.io/v1/devices/3d0022001847353236343033/led?access_token=1628837bbea99939ecdfcdf819e506db59d79753");

				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				String urlParameters = "agrs=on";
				connection.setRequestMethod("POST");
				connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
				connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
				connection.setDoOutput(true);
				DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
				dStream.writeBytes(urlParameters);
				dStream.flush();
				dStream.close();
				int responseCode = connection.getResponseCode();

				System.out.println("\nSending 'POST' request to URL : " + url);
				System.out.println("Post parameters : " + urlParameters);
				System.out.println("Response Code : " + responseCode);

				final StringBuilder output = new StringBuilder("Request URL " + url);
				output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
				output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
				output.append(System.getProperty("line.separator")  + "Type " + "POST");
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = "";
				StringBuilder responseOutput = new StringBuilder();
				System.out.println("output===============" + br);
				while((line = br.readLine()) != null ) {
					responseOutput.append(line);
				}
				br.close();

				output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute() {
			progress.dismiss();
			Toast.makeText(context, "Light turned on", Toast.LENGTH_SHORT).show();
		}

	}
}
