package com.tokopedia.tkpd.network;

import org.json.JSONException;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.util.JSONHandler;
import com.tokopedia.tkpd.util.StringVariable;
import com.tokopedia.tkpd.util.TokenHandler;
import com.tokopedia.tkpd.var.TkpdUrl;

import android.content.Context;
import android.os.AsyncTask;

@Deprecated
public class TalkNetworkhandler {
	private Context context;
	private JSONHandler JsonSenderFollow;
	private JSONHandler JsonSenderDelete;
	private JSONHandler JsonSenderGet;
	private JSONHandler JsonSender;
	private TokenHandler Token = new TokenHandler();
	
	public TalkNetworkhandler (Context context) {
		this.context = context;
	}
	
	
	public class DeleteTalk extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
	            int TokenStatus = Token.checkToken(context);     
	            if (TokenStatus==1 || TokenStatus==2) {
	            	JsonSenderDelete = new JSONHandler(TkpdUrl.GET_TALK);
	            	JsonSenderDelete.AddJSON("app_id", Token.getAppId(context));
	            	JsonSenderDelete.AddJSON("token", Token.getToken(context));
	            	JsonSenderDelete.AddJSON("user_id", Token.getLoginID(context));
	            	JsonSenderDelete.AddJSON("act", "delete_talk");
	            	JsonSenderDelete.AddJSON("id", params[0]);
	            	JsonSenderDelete.CompileJSON();
	            }
			} catch (JSONException e) {
						// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String response = null;
			for (int i=0;i<5 && response==null;i++) {
				response = JsonSenderDelete.getResponse();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return response;
			
		}

		@Override
		protected void onPostExecute(String response) {
			System.out.println(response);
			
		}
		
	}

}
