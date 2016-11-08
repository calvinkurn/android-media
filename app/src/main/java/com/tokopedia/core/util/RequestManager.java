package com.tokopedia.core.util;

import java.util.ArrayList;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.tokopedia.core.network.NetworkHandler.CustomStringRequest;
import com.tokopedia.core.network.NetworkHandler.RetryPolicyVar;
import com.tokopedia.core.network.VolleyRequestQueue;

import android.content.Context;

/**
 * Manager for the queue
 * 
 * @author Trey Robinson
 *
 */
public class RequestManager {
	
	/**
	 * the queue :-)
	 */
	private static RequestQueue mRequestQueue;
	public static int NORMAL_NETWORK_TAG = 0;
	public static int TOKEN_NETWORK_TAG = 1;
	private static Boolean isRequestToken = false;
	private static Boolean isDialogShown = false;
	private static Boolean isMaintenanceDialogShown = false;
	private static ArrayList<CustomStringRequest> ListRequest= new ArrayList<CustomStringRequest>();
	private static ArrayList<RetryPolicyVar> ListRequestVar = new ArrayList<RetryPolicyVar>();
	private static ArrayList<String> RequestID = new ArrayList<String>();
	private static ArrayList<String> RequestClass = new ArrayList<String>();
	private static String CurrClassName;
	/**
	 * Nothing to see here.
	 */
	private RequestManager() {
	 // no instances
	} 

	/**
	 * @param context
	 * 			application context
	 */
	public static void init(Context context) {
		mRequestQueue = VolleyRequestQueue.newRequestQueue(context);
	}
	
	public static void setRequestTokenStats (Boolean param) {
		isRequestToken = param;
		
	}
	
	public static Boolean getRequestTokenStats () {
		return isRequestToken;
	}
	
	public static void setDialogStats (Boolean param) {
		isDialogShown = param;
		
	}
	
	public static Boolean getDialogStats () {
		return isDialogShown;
	}
	
	public static void setDialogMaintenanceStats (Boolean param) {
		isMaintenanceDialogShown = param;
		
	}
	
	public static Boolean getDialogMaintenanceStats () {
		return isMaintenanceDialogShown;
	}
	
	public static void setCurrClassName (String param) {
		CurrClassName = param;
	}
	
	public static String getCurrClassName () {
		return CurrClassName;
	}
	

	/**
	 * @return
	 * 		instance of the queue
	 * @throws
	 * 		IllegalStatException if init has not yet been called
	 */
	public static RequestQueue getRequestQueue() {
	    if (mRequestQueue != null) {
	        return mRequestQueue;
	    } else {
	        throw new IllegalStateException("Not initialized");
	    }
	}
	
	public static void cancelAllRequest () {
	    if (mRequestQueue != null) {
	    	//mRequestQueue.cancelAll(NETWORK_TAG);
	    } else {
	        throw new IllegalStateException("Not initialized");
	    }
	}
	
	public static ArrayList<CustomStringRequest> getRequestList() {
		return ListRequest;
	}
	
	public static void addRequestList(CustomStringRequest request, String id, String name, RetryPolicyVar retry) {
		ListRequest.add(request);
		RequestID.add(id);	
		RequestClass.add(name);
		ListRequestVar.add(retry);
	}
	
	public static void removeRequestList(String id) {
		for (int i = 0; i < RequestID.size(); i++) {
			if (RequestID.get(i).equals(id)) {
				ListRequest.remove(i);
				RequestID.remove(i);
				RequestClass.remove(i);
				ListRequestVar.remove(i);
			}
		}
	}
	
	public static void clearRequestList (String name) {
		for (int i = 0; i < RequestClass.size(); i++) {
			if (RequestClass.get(i).equals(name)) {
				ListRequest.remove(i);
				RequestID.remove(i);
				RequestClass.remove(i);
				ListRequestVar.remove(i);
			}
		}
	}
	
	public static void retryRequestList (String name) {
		for (int i = 0; i < RequestClass.size(); i++) {
			if (RequestClass.get(i).equals(name)) {
				ListRequest.get(i).setRetryPolicy(new DefaultRetryPolicy(
						ListRequestVar.get(i).Timeoutc,
						ListRequestVar.get(i).MaxRetriesc,
						ListRequestVar.get(i).BackoffMultic));
				getRequestQueue().add(ListRequest.get(i));
				ListRequest.remove(i);
				RequestID.remove(i);
				RequestClass.remove(i);
				ListRequestVar.remove(i);
			}
		}
	}

}
