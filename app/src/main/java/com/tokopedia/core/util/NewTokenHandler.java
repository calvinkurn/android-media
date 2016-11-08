
package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;



/**
 * @author WD-05
 *
 */
public class NewTokenHandler  {
	
	private JSONHandler jsonclient;
	private static String uniqueID = null;
	private static String tokenID = null; 
	private static String time = null;
	private static final String TOKEN_STORAGE = "TOKEN_STORAGE";
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
	private static final String TOKEN_ID = "TOKEN_ID";
	private static final String TIMESTAMP = "TIMESTAMP";
	private static final String VERSION_STORAGE = "VERSION_STORAGE";
	private Context context;
	private TokenHandlerListener tokenListener;
	private int Status;
	private WaitRequestToken reqtoken;
	//private static final int time;
	
	public interface TokenHandlerListener {
		public void onSuccess (int status);
	}

	public NewTokenHandler(Context context) {
		this.context = context;
	}
	
	public interface WaitRequestToken {
		public void onFinish();
	}

	public synchronized Integer ATGenerator(TokenHandlerListener listener) {
		this.tokenListener = listener;
		Status = 0;
	    if (uniqueID == null) {
	        final SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
	        final SharedPreferences sharedPrefsVer = context.getSharedPreferences(VERSION_STORAGE, Context.MODE_PRIVATE);
	        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
	        if (uniqueID == null) {
	            uniqueID = UUID.randomUUID().toString();
	            Editor editor = sharedPrefs.edit();
        		editor.putString(PREF_UNIQUE_ID, uniqueID);
        		editor.commit();
        		NetworkHandler network = new NetworkHandler(context, TkpdUrl.REQUEST_TOKEN);
        		network.AddParam("app_id", uniqueID);
        		network.Commit(new NetworkHandlerListener(){

					@Override
					public void onSuccess(Boolean status) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void getResponse(JSONObject Result) {
						try {
							tokenID = Result.getString("token");
		            	} catch (JSONException e) {
		            		e.printStackTrace();
		            	}
		            	Long tsLong = System.currentTimeMillis()/1000;
		            	time = tsLong.toString();
		            	if (tokenID.length() == 36) {
		            		Editor editor = sharedPrefs.edit();
		            		editor.putString(PREF_UNIQUE_ID, uniqueID);
		            		editor.putString(TIMESTAMP, time);
		            		editor.putString(TOKEN_ID, tokenID);
		            		editor.commit();
		            		
		            		Editor editorVer = sharedPrefsVer.edit();
		            		try {
								PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
								int versionCode = pInfo.versionCode;
								editorVer.putInt("VERSION_CODE", versionCode);
								editorVer.commit();
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            	} else {
		            		Status = 0;
		            	}	
		            	tokenListener.onSuccess(1);
					}

					@Override
					public void getMessageError(ArrayList<String> MessageError) {
						// TODO Auto-generated method stub
						
					}
        			
        		});
	        }
	    }    
		return Status;

	}

	public synchronized void RefreshAT(TokenHandlerListener listener) {

	    final SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
	    final SharedPreferences sharedPrefsVer = context.getSharedPreferences(VERSION_STORAGE, Context.MODE_PRIVATE);
	    sharedPrefs.edit().clear().commit();
	    sharedPrefsVer.edit().clear().commit();
	    uniqueID = null;
	    ATGenerator(listener);

	}

	public synchronized Integer TokenGenerator(TokenHandlerListener listener) {
//		this.tokenListener = listener;
//		Status = 0; //0 = failed, 1 = success, 3 = wait for another request
//	    final SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
//	    uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
//	    CommonUtils.dumper(RequestManager.getRequestTokenStats());
//	    if (!RequestManager.getRequestTokenStats()) {
//		    NetworkHandler network = new NetworkHandler(context, TkpdUrl.REQUEST_TOKEN);
//		    network.AddParam("app_id", uniqueID);
//		    network.setTag(RequestManager.TOKEN_NETWORK_TAG);
//	        network.Execute(new NetworkHandlerListener(){
//
//
//				@Override
//				public void onSuccess(Boolean status) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void getResponse(JSONObject Result) {
//					try {
//						tokenID = Result.getString("token");
//		        	} catch (JSONException e) {
//		        		e.printStackTrace();
//		        	}
//		        	Long tsLong = System.currentTimeMillis()/1000;
//		        	time = tsLong.toString();
//		        	if (tokenID.length() == 36) {
//		        		Editor editor = sharedPrefs.edit();
//		        		editor.putString(PREF_UNIQUE_ID, uniqueID);
//		        		editor.putString(TIMESTAMP, time);
//		        		editor.putString(TOKEN_ID, tokenID);
//		        		editor.commit();
//		        	} else {
//		        		Status = 0;
//		        	}
//		        	tokenListener.onSuccess(Status);
//
//				}
//
//				@Override
//				public void getMessageError(ArrayList<String> MessageError) {
//					// TODO Auto-generated method stub
//
//				}
//	        });
//	    } else {
//	    	tokenListener.onSuccess(3);
//	    }
		return Status;

}


	
	public synchronized Long getTime(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 time = sharedPrefs.getString(TIMESTAMP, null);
		 Long time_int = Long.parseLong(time);
		 return time_int;
	}
	
	
	public synchronized String getToken() {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 tokenID = sharedPrefs.getString(TOKEN_ID, null);
		 return tokenID;
	}
	
	public synchronized String getAppId() {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
		 return uniqueID;
	}
	
	public static String getAppId(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 return sharedPrefs.getString(PREF_UNIQUE_ID, null);
	}
	
	public static String getToken(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 return sharedPrefs.getString(TOKEN_ID, "bego lo");
	}
	
	public synchronized Integer getVersionCode(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(VERSION_STORAGE, Context.MODE_PRIVATE);
		 return sharedPrefs.getInt("VERSION_CODE", 0);
	}

	 public Integer checkToken() {
		 	int status = 0; //0 = failed to request token , 1 = successfully request new token, 2 = token has not expired yet
	    	String token_id = getToken(); 
	    	if (token_id!=null) {
	    		Long time = getTime(context);
	    		Long curr_time = System.currentTimeMillis()/1000;
	        	Long interval = curr_time - time;
	        	System.out.println("interval: " +interval);
	        	if (interval > 900) {
	        		status = 1;
	        	}else {
	        		status = 2;
	        	}    	
	    	}
	    	return status;    	
	 	}
	 
	 public void WaitForToken (WaitRequestToken reqtoken) {
		 this.reqtoken = reqtoken;
		 new TokenChecker().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	 }
	 
	 public class TokenChecker extends AsyncTask<Void, Boolean, Boolean> {

			@Override
			protected Boolean doInBackground(Void... params) {
				int loop = 0;
				while (RequestManager.getRequestTokenStats() && loop < 15) {
					try {
						Thread.sleep(1000);
						CommonUtils.dumper("Loop: "+loop);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					loop++;
				}
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean status) {
				RequestManager.setRequestTokenStats(false);
				reqtoken.onFinish(); 
				
			}
			 
		 }
	 

	}
