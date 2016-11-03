
package com.tokopedia.tkpd.util;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.var.TkpdUrl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;



/**
 * @author WD-05
 *
 */
public class TokenHandler  {
	
	private JSONHandler jsonclient;
	private static String uniqueID = null;
	private static String tokenID = null;
	private static String time = null;
	private static final String TOKEN_STORAGE = "TOKEN_STORAGE";
	private static final String VERSION_STORAGE = "VERSION_STORAGE";
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
	private static final String TOKEN_ID = "TOKEN_ID";
	private static final String TIMESTAMP = "TIMESTAMP";
	private static final String LOGIN_SESSION = "LOGIN_SESSION";
	private static final String LOGIN_ID = "LOGIN_ID";
	//private static final int time;


	public synchronized Integer ATGenerator(Context context) {
		jsonclient = new JSONHandler(TkpdUrl.REQUEST_TOKEN);
		String temp_json = null;
		Integer status = 0;
	    if (uniqueID == null) {
	        SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
	        SharedPreferences sharedPrefsVer = context.getSharedPreferences(VERSION_STORAGE, Context.MODE_PRIVATE);
	        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
	        if (uniqueID == null) {
	            uniqueID = UUID.randomUUID().toString();
	            try {
	    			jsonclient.AddJSON("app_id", uniqueID);
	    			
	    		} catch (JSONException e1) {
	    			e1.printStackTrace();
	    		}
	            try {
					status = jsonclient.CompileJSON();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            if (status==1) {
	            	String response = jsonclient.getResponse();
	            	System.out.println("uuid_response: " +response);
	            	try {
	            		JSONObject json = new JSONObject(response);
	            		temp_json = json.getString("result");
	            		JSONObject json2 = new JSONObject(temp_json);
	            		tokenID = json2.getString("token");
	            	} catch (JSONException e) {
					e.printStackTrace();
	            	}
	            	Long tsLong = System.currentTimeMillis()/1000;
	            	time = tsLong.toString();
	            	System.out.println("time : " +time);
	            	}
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
	            	}else{
	             		status=0;
	             	}
	            }
	        }
		return status;
	    }
	
	public synchronized Integer TokenGenerator(Context context) {
		jsonclient = new JSONHandler(TkpdUrl.REQUEST_TOKEN);
		int status = 0;
		String temp_json = null;
		SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
	    uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
	    try {
 			jsonclient.AddJSON("app_id", uniqueID);
 			
 		} catch (JSONException e1) {
 			e1.printStackTrace();
 		}
        try {
        	status =  jsonclient.CompileJSON();
        	System.out.println("status: " +status);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        if (status==1) {
        	String response = jsonclient.getResponse();
        	String token = null;
         	System.out.println("uuid_response: " +response);
         	try {
         		JSONObject json = new JSONObject(response);
        		temp_json = json.getString("result");
        		JSONObject json2 = new JSONObject(temp_json);
        		token = json2.getString("token");
         	} catch (JSONException e) {
				e.printStackTrace();
         	}
         	Long tsLong = System.currentTimeMillis()/1000;
         	String ts = tsLong.toString();
         	System.out.println("time: " +ts);
         	Editor editor = sharedPrefs.edit();
         	editor.putString(TIMESTAMP, ts);
         	editor.putString(TOKEN_ID, token);
         	editor.commit();
        }
	return status;
}
	
	public void SetLoginSession(Context context, String u_id) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
		 Editor editor = sharedPrefs.edit();
		 editor.putString(LOGIN_ID, u_id);
		 editor.commit();
		 //return status;
	}
	
	public synchronized String getLoginID(Context context) {
		 String u_id = null;
		 SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
		 u_id = sharedPrefs.getString(LOGIN_ID, null);
		 return u_id;
	}
	
	public synchronized Long getTime(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 time = sharedPrefs.getString(TIMESTAMP, null);
		 Long time_int = Long.parseLong(time);
		 return time_int;
	}
	
	
	public synchronized String getToken(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 tokenID = sharedPrefs.getString(TOKEN_ID, null);
		 return tokenID;
	}
	
	public synchronized String getAppId(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
		 uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
		 return uniqueID;
	}
	
	public synchronized Integer getVersionCode(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(VERSION_STORAGE, Context.MODE_PRIVATE);
		 return sharedPrefs.getInt("VERSION_CODE", 0);
	}
	
	 public Integer checkToken(Context context) {
		 	int status = 0; //0 = failed to request token , 1 = successfully request new token, 2 = token has not expired yet
	    	String token_id = getToken(context); 
	    	if (token_id!=null) {
	    		Long time = getTime(context);
	    		Long curr_time = System.currentTimeMillis()/1000;
	        	Long interval = curr_time - time;
	        	System.out.println("interval: " +interval);
	        	if (interval>900) {
	        		status = TokenGenerator(context);
	        	}else {
	        		status = 2;
	        	}    	
	    	}
	    	return status;    	
	 	}

	}
