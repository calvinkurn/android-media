package com.tokopedia.tkpd.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.Logger;
import com.tkpd.library.utils.ToastNetworkHandler;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.util.RetryHandler;
import com.tokopedia.tkpd.util.RetryHandler.OnRetryListener;
import com.tokopedia.tkpd.MaintenancePage;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.SplashScreen;
import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.app.TkpdFragmentActivity;
import com.tokopedia.tkpd.util.EncoderDecoder;
import com.tokopedia.tkpd.util.NewTokenHandler;
import com.tokopedia.tkpd.util.NewTokenHandler.TokenHandlerListener;
import com.tokopedia.tkpd.util.NewTokenHandler.WaitRequestToken;
import com.tokopedia.tkpd.util.PasswordGenerator;
import com.tokopedia.tkpd.util.RequestManager;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.analytics.TrackingUtils;
import com.tokopedia.tkpd.var.TkpdCache;
import com.tokopedia.tkpd.var.TkpdState;

@Deprecated
public final class NetworkHandler {
	private static final String TAG = NetworkHandler.class.getSimpleName();
	private EncoderDecoder encoder;
	private String IDunique = null;
	private JSONObject JsonPutter = new JSONObject();
	private Context context;
	private String url;
	private NetworkHandlerListener netwrokListener;
	private CustomStringRequest postRequest;
	private String postRequestID;
	//private int Timeout = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
	private int Timeout = 10000;
	private int MaxRetries = 2;
	private float BackoffMulti = 1.0f;
	private int TimeoutDefault = 10000;
	//private RequestQueue queue;
	private NewTokenHandler Token;
	private PasswordGenerator Password;
	private SessionHandler Session;
	private NetworkHandler network;
	private RetryPolicyVar retryVar;
	private int RetryCountServerErr = 0;
    private int RetryNoConnectionErr = 0;
	private Boolean DisableDialog = false;
	private int Delay = 10000;
	private NetworkConfig config;
	private RetryHandler retryHandler;
	private Boolean isDisabledRetry = false;
	private OnTimeOutListener timeOutListener;
	private Map<String,String> headerMap;

	private int NetworkTag = RequestManager.NORMAL_NETWORK_TAG;

	public void AddHeader(String key, String value) {
		headerMap.put(key, value);
	}

	public interface NetworkHandlerListener {
		public void onSuccess(Boolean status);
		public void getResponse(JSONObject Result);
		public void getMessageError(ArrayList<String> MessageError);
	}

	public interface OnTimeOutListener{
		void onNetworkTimeOut();
	}

	public interface OnRetryRequestListener{
		void onRetryRequest();
	}

	public void setTimeOutListener(OnTimeOutListener timeOutListener){
		this.timeOutListener = timeOutListener;
	}

	public OnRetryRequestListener getOnRetryRequestInterface(){
		return new OnRetryRequestListener() {
			@Override
			public void onRetryRequest() {
				RetryRequest();
			}
		};
	}
	
	public NetworkHandler(Context context, String url) {
//		Debug.startMethodTracing("temp"); // TODO Start here
        encoder = new EncoderDecoder();
		this.context = context;
		this.url = TkpdNetworkURLHandler.generateURL(context, url);
		Token = new NewTokenHandler(context);
		Session = new SessionHandler(context);
		Password = new PasswordGenerator(context);
		network = this;
		headerMap = new HashMap<>();
		getConfigRetry();
		//queue = Volley.newRequestQueue(context);
	}

    public NetworkHandler(Context context, String url, Boolean isUseCustomDomain) {
//		Debug.startMethodTracing("temp"); // TODO Start here
        encoder = new EncoderDecoder();
        this.context = context;
        if (isUseCustomDomain) this.url = TkpdNetworkURLHandler.generateURL(context, url);
        else this.url = url;
        Token = new NewTokenHandler(context);
        Session = new SessionHandler(context);
        Password = new PasswordGenerator(context);
        network = this;
        getConfigRetry();
        //queue = Volley.newRequestQueue(context);
    }

	
	public void setRetryHandler(RetryHandler retry) {
		this.retryHandler = retry;
		retryHandler.setOnRetryListener(new OnRetryListener() {

			@Override
			public void onRetryCliked() {
				RetryRequest();
				retryHandler.disableRetryView();
			}
		});
	}
	
	public void setRetryHandler(RetryHandler retry, OnRetryListener listener) {
		this.retryHandler = retry;
		retryHandler.setOnRetryListener(listener);
	}
	
	public void disableRetry() {
		isDisabledRetry = true;
	}

	
	private void getConfigRetry() {
		config = new NetworkConfig(context, url);
		Timeout = config.getTimeout();
		MaxRetries = config.getMaxRetries();
		BackoffMulti = config.getBackOffMulti();
		if (Timeout < 5000) {
			Timeout = TimeoutDefault;
		}
		CommonUtils.dumper("timeout: " + Timeout + " retries: " + MaxRetries + " backoff: " + BackoffMulti);
	}
	
	public Boolean setDisableDialog(Boolean status) {
		this.DisableDialog = status;
		return DisableDialog;
	}
	
	public void AddParam(String key, String value) {
		try {
			JsonPutter.put(key,value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void AddParam(String key, Boolean value) {
		try {
			JsonPutter.put(key,value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void AddParam(String key, Integer value) {
		try {
			JsonPutter.put(key,value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public void AddParam(String key, Object value) {
        try {
            JsonPutter.put(key,value.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddParam(NetworkParamHandler param) {
        JSONObject json = param.getJSONObject();
        for(Iterator<String> iter = json.keys();iter.hasNext();) {
            String key = iter.next();
            try {
                AddParam(key, json.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
	
	public void Commit(NetworkHandlerListener listener) {
		this.netwrokListener = listener;
		//int status = Token.checkToken();
		int status = 2;
		//CommonUtils.dumper(PasswordGenerator.getUniquePsuedoID());
		if (status == 2) {
			AddParam("app_id", Password.getAppId());
            AddParam("app_version", BuildConfig.VERSION_CODE);
//			String Lang = Locale.getDefault().getLanguage();
//			if (!Lang.equals("en")) {
				String Lang = "id";
//			}
			AddParam("lang", Lang);
			if (SessionHandler.isV4Login(context)) AddParam("user_id", Session.getLoginID());
			Execute(netwrokListener);
		} else {
			Token.TokenGenerator(new TokenHandlerListener(){

				@Override
				public void onSuccess(int status) {			
					if (status == 3) {
						Token.WaitForToken(new WaitRequestToken(){

							@Override
							public void onFinish() {;
								AddParam("app_id", Token.getAppId());
								AddParam("token", Token.getToken());
                                if (SessionHandler.isV4Login(context)) AddParam("user_id", Session.getLoginID());
								NetworkTag = RequestManager.NORMAL_NETWORK_TAG;
								Commit(netwrokListener);	
							}
							
						});
					} else {
						AddParam("app_id", Token.getAppId());
						AddParam("token", Token.getToken());
                        if (SessionHandler.isV4Login(context)) AddParam("user_id", Session.getLoginID());
		        		RequestManager.setRequestTokenStats(false);	
		        		NetworkTag = RequestManager.NORMAL_NETWORK_TAG;
		        		Commit(netwrokListener);	
					}
					
				}
				
			});
		}
	}
	
	public void setTag (int tag) {
		NetworkTag = tag;
	}

	private void RetryRequest() {
		postRequest.setRetryPolicy(new  DefaultRetryPolicy(
					  Timeout, 
		              MaxRetries, 
		              BackoffMulti));
		RequestManager.getRequestQueue().add(postRequest);
	}
	
	//public void setTimeout (int timeout) {
		//this.Timeout = timeout;
	//}
	
	public void setRetryPolicy(int timeout, int MaxRetries, float backoffmulti) {
		this.Timeout = timeout;
		this.MaxRetries = MaxRetries;
		this.BackoffMulti = backoffmulti;
	}
	
	public int getTimeout() {
		return Timeout;
	}
	
	public int getMaxAttempt() {
		return MaxRetries;
	}
	
	public float getBackoffMulti() {
		return BackoffMulti;
	}

    public void printWS(){
        System.out.println("sending to ws:"+JsonPutter.toString());
    }
	
	public void Execute(NetworkHandlerListener listener) {
		this.netwrokListener = listener;
		IDunique = UUID.randomUUID().toString();
		postRequestID = UUID.randomUUID().toString();
    	String id = IDunique.replaceAll("-", "");
    	final String iv = id.substring(0, 16);
    	if (Password.getAppId() != null && Password.getSignature() != null) {
    		AddParam("token", Password.generatePassword(iv.substring(8,16)));
    	}

		JSONObject jsonTemp= null;
		try {
			jsonTemp = new JSONObject(JsonPutter.toString());
			if(jsonTemp.has("card_number")){
				jsonTemp.putOpt("card_number", CommonUtils.replaceCreditCardNumber(jsonTemp.optString("card_number")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("MNORMANSYAH", "sending to ws:" + jsonTemp.toString());
		Log.d("MNORMANSYAH", "URL:" + url);
    	final String encrypt_data = encoder.Encrypt(JsonPutter.toString(),iv);
    	postRequest = new CustomStringRequest(Request.Method.POST, url,
    		    new Response.Listener<String>()
    		    {
    		        @Override
    		        public void onResponse(String response) {
//    		        	Debug.stopMethodTracing(); // TODO Stop here
    		        	CommonUtils.dumper("Response Size: "+response.getBytes().length);
    		        	RequestManager.removeRequestList(postRequestID);
    		        	//System.out.println("Response From: "+url);
    		        	Boolean Status = true;
						try {                
	    		        	JSONObject JsonResponse = new JSONObject(response);
	    		        	if (!JsonResponse.isNull("config")) {
	    		        		JSONObject RetryConfig = new JSONObject(JsonResponse.getString("config"));
	    		        		config.UpdateConfigFromJSON(RetryConfig);
	    		        	}
	    		        	
	    		        	Logger.i("Network Response", JsonResponse.toString(1));
							Log.d("MNORMANSYAH", "old Network Handler : " + JsonResponse.toString());
							if(MaintenancePage.isMaintenance(context))
								netwrokListener.getResponse(JsonResponse);
	    		        	if (JsonResponse.getString("status").equals("UNDER_MAINTENANCE")) {
								if(!MaintenancePage.isMaintenance(context)) {
									context.startActivity(MaintenancePage.createIntentFromNetwork(context, JsonResponse.optString("message", "")));
								}
	    		        	}

	    		        	if (!JsonResponse.isNull("result") && !JsonResponse.getString("result").equals("0")) {
	    		        		try {
		    		        		JSONObject Result = new JSONObject(JsonResponse.getString("result"));
		    		        		//System.out.println(Result.toString(1));
		    		        		netwrokListener.getResponse(Result);
	    		        		} catch (OutOfMemoryError e) {
	    		        			Status = false;
	    		        		} catch (Exception e) {
	    		        			Status = false;
	    		        		}
 	    		        	} else {
	    		        		Status = false;
	    		        	}
	    		        	if (!JsonResponse.isNull("message_error")) {
	    		        		JSONArray JsonErrorList = new JSONArray(JsonResponse.getString("message_error"));
	    		        		ArrayList<String> ErrorList = new ArrayList<String>();
	    		        		for (int i = 0; i < JsonErrorList.length(); i++) {
	    		        			ErrorList.add(JsonErrorList.getString(i));
	    		        		}
	    		        		if (ErrorList.size() > 0)
	    		        			netwrokListener.getMessageError(ErrorList);
	    		        	}
	    		        	if(!JsonResponse.isNull("status"))
	    		        		if(isRequestDenied(JsonResponse)) {
									Log.i(NetworkHandler.class.getSimpleName(), NetworkHandler.class.getSimpleName()+" expired login ["+url+"]");
									forceLogout();
                                }
	    		        	netwrokListener.onSuccess(Status);
						} catch (JSONException e) {
							Log.e("Network Error", response);
							Logger.i("network error", response);
							e.printStackTrace();
						} catch (OutOfMemoryError e) {
							Status = false;
						} catch (Exception e) {
							Status = false;
						}	
    		        }
    		    },
    		    new Response.ErrorListener()
    		    {
    		         @Override
    		         public void onErrorResponse(VolleyError error) {
    		        	 CommonUtils.dumper("Timeout from: "+url);
                         if(retryHandler == null){
                             CommonUtils.dumper("NISIETAGCONNECTION RETRYHANDLER IS NULL");
                         }else
                         {
                             CommonUtils.dumper("NISIETAGCONNECTION RETRYHANDLER IS NOT NULL");
                         }

						 if(timeOutListener!=null && error.toString().equals("com.android.volley.TimeoutError")){
							 timeOutListener.onNetworkTimeOut();
						 }

    		        	 if (retryHandler != null && error.toString().equals("com.android.volley.TimeoutError")) {
    		        		 retryHandler.enableRetryView();
    		        	 } else if (!isDisabledRetry) {
    		        		 handleError(error);
    		        	 }
    		    }

			
    		    }
    		) {    
    		    @Override
    		    protected Map<String, String> getParams()
    		    { 
    		            Map<String, String>  params = new HashMap<String, String>();
    		            //CommonUtils.dumper(u(rl);
    		            CommonUtils.dumper("iv result : " +  iv);
    		            CommonUtils.dumper("sc result : " +  encrypt_data);
    		            params.put("sc", encrypt_data);
    		            params.put("iv", iv);    
    		            return params; 
    		    }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = super.getHeaders();

                    if (headers == null
                            || headers.equals(Collections.emptyMap())) {
                        headers = new HashMap<String, String>();
                    }

                    headers.put("X-APP-VERSION", Integer.toString(BuildConfig.VERSION_CODE));
					for (String key : headerMap.keySet()) {
						headers.put(key, headerMap.get(key));
					}
					return headers;
                }
    		};
    	if (NetworkTag == RequestManager.TOKEN_NETWORK_TAG) {
    		if (!RequestManager.getRequestTokenStats()) {
    			RequestManager.setRequestTokenStats(true);
    			postRequest.setTag(NetworkTag);
    			postRequest.setPriority(Priority.HIGH);
    	    	RequestManager.getRequestQueue().add(postRequest);
    		}
    	} else {
    		postRequest.setTag(NetworkTag);
        	RequestManager.getRequestQueue().add(postRequest);
    	}
	}

    private Boolean isRequestDenied(JSONObject json) throws JSONException {
        return json.getString("status").equals("REQUEST_DENIED");
    }

	public static void forceLogout(Context context){
		Log.d("Network Handler", "forceLogout");
		SessionHandler session = new SessionHandler(context);
		session.forceLogout();
		LocalCacheHandler.clearCache(context, TkpdCache.INDEX);
		PasswordGenerator.clearTokenStorage(context);
        Intent intent = new Intent(context, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}

    private void forceLogout() {
		Log.d("Network Handler", "forceLogout");
        SessionHandler session = new SessionHandler(context);
        session.forceLogout();
        LocalCacheHandler.clearCache(context, TkpdCache.INDEX);
        PasswordGenerator.clearTokenStorage(context);
        Intent intent = new Intent(context, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
	
	private void handleError(final VolleyError error) {
		TrackingUtils.eventNetworkError(error.toString());
		RequestManager.addRequestList(postRequest, postRequestID, context.getClass().getSimpleName(), retryVar);
     	Boolean isPause = false;
     	Boolean Valid = false;
     	int NetworkState = 0;
     	int errorCode = 0;
     	CommonUtils.dumper("Error: "+error);
     	if (error instanceof ServerError) {
     		errorCode = error.networkResponse.statusCode;
     		NetworkState = TkpdState.NetworkState.SERVER_ERROR;
     	} else if (error instanceof NoConnectionError) {
     		NetworkState = TkpdState.NetworkState.NO_CONNECTION;
     	} else {
     		NetworkState = TkpdState.NetworkState.TIMEOUT;
     	}
    	 try {
    		 CommonUtils.dumper("STATE: "+MainApplication.getActivityState());
        	 if (MainApplication.getActivityState() == TkpdState.Application.ACTIVITY) {
        		 isPause = ((TActivity) context).isPausing();
        		 Valid = true;
        	 } else if (MainApplication.getActivityState() == TkpdState.Application.FRAGMENT_ACTIVITY) {
        		 isPause = ((TkpdFragmentActivity) context).isPausing();
        		 Valid = true;
        	 }
        	 CommonUtils.dumper("valid? "+Valid+" isPause? "+isPause+"Prev Class: "+RequestManager.getCurrClassName()+"Curr Class: "+context.getClass().getSimpleName()+"Dialog Stat? "+RequestManager.getDialogStats());
        	 if (Valid && !isPause) {
        		 Boolean AllowDialog = false;
        		 if (errorCode == 500) {
        			 //Log.e("Network Error", "Error Response: ");
        			 Log.e("Network Error", "Error "+errorCode+" From "+url+" Param: "+JsonPutter.toString(1));
        			 if (RetryCountServerErr <= 5) {
	        			 CommonUtils.dumper("udah masuk ke syarat");		 
		        		 CommonUtils.dumper("allow dialog: "+AllowDialog);
		        		 if (RequestManager.getCurrClassName() == null) {
		        			 AllowDialog = true;
		        		 } else if (!RequestManager.getCurrClassName().equals(context.getClass().getSimpleName())) {
		        			 AllowDialog = true;
		        		 } 
		        		 RequestManager.setCurrClassName(context.getClass().getSimpleName());
		        		 RequestManager.retryRequestList(context.getClass().getSimpleName());
						 RequestManager.clearRequestList(context.getClass().getSimpleName());
						 RetryCountServerErr++;
        			 } else {
        				 if (MainApplication.getActivityState() == TkpdState.Application.ACTIVITY) {
		        		 ((TActivity) context).finish();
		        	 } else if (MainApplication.getActivityState() == TkpdState.Application.FRAGMENT_ACTIVITY) {
		        		 try {
    		        		 ((TkpdFragmentActivity) context).finish();
						} catch (ClassCastException e) {
							CommonUtils.dumper(e.getMessage());
						}
		        		 
		        	 } 
        			 }
        		 /*} else if (NetworkState == TkpdState.NetworkState.TIMEOUT){
        			 RequestManager.setCurrClassName(context.getClass().getSimpleName());
        			 RequestManager.retryRequestList(context.getClass().getSimpleName());
					 RequestManager.clearRequestList(context.getClass().getSimpleName());*/
        		 } else if (NetworkState == TkpdState.NetworkState.NO_CONNECTION || NetworkState == TkpdState.NetworkState.TIMEOUT) {
        			 if (NetworkState == TkpdState.NetworkState.NO_CONNECTION && RetryNoConnectionErr < 3) {
                         CommonUtils.dumper("Retry no connection......");
						 if(netwrokListener != null){
							 ArrayList<String> list = new ArrayList<>();
							 list.add(context.getString(R.string.error_no_connection2));
							 netwrokListener.getMessageError(list);

							 /**
							  * Creator : Hafizh H
							  * Error from volley will no longer visible in app but still can be seen in log
							  */
							 Log.e(TAG,"volley error "+ error.getMessage());
							 error.printStackTrace();
						 }

						 // TODO RADITYA
						 // Why do this?
						 // create postdelay and run it in Main Thread over and over again
						 // will causes ANR
						 /*Handler handler = new Handler();
                         handler.postDelayed(new Runnable() {

                             @Override
                             public void run() {
                                 RequestManager.retryRequestList(context.getClass().getSimpleName());
                                 RequestManager.clearRequestList(context.getClass().getSimpleName());
                                 RetryNoConnectionErr++;
                             }
                         }, 2000);*/

                     } else {
                         CommonUtils.dumper("udah masuk ke syarat");
                         CommonUtils.dumper("allow dialog: " + AllowDialog);
                         if (RequestManager.getCurrClassName() == null) {
                             AllowDialog = true;
                         } else if (!RequestManager.getCurrClassName().equals(context.getClass().getSimpleName())) {
                             AllowDialog = true;
                         } else {
                             if (RequestManager.getDialogStats()) {
                                 AllowDialog = false;
                             } else {
                                 AllowDialog = true;
                             }
                         }
                     }
        		 }
        		 CommonUtils.dumper("allow dialog: "+AllowDialog);
        		 if (AllowDialog && !DisableDialog) {
        			 CommonUtils.dumper(NetworkState);
        			 if (NetworkState == TkpdState.NetworkState.SERVER_ERROR) {	 
		        		 ToastNetworkHandler.showToast(context);
		        		 //Toast.makeText(context, "there is a pproblem with a network connection", Toast.LENGTH_LONG);		
        			 } else if (NetworkState == TkpdState.NetworkState.TIMEOUT) {
        				 if (retryHandler != null) {
        					 retryHandler.enableRetryView();
        				 } else {
	        				 RequestManager.setDialogStats(true);
	        				 CommonUtils.UniversalToast(context, context.getResources().getString(R.string.msg_connection_problem)+" "+(Delay/1000)+" detik");
	        				 Handler handler = new Handler();
	        				 handler.postDelayed(new Runnable() {
								
								@Override
								public void run() {
									RequestManager.setDialogStats(false);
									RequestManager.retryRequestList(context.getClass().getSimpleName());
									RequestManager.clearRequestList(context.getClass().getSimpleName());
									
								}
							}, Delay);
        				 }
        				//Delay = Delay+10000;
        			 } else if (NetworkState == TkpdState.NetworkState.NO_CONNECTION) {
        				RequestManager.setCurrClassName(context.getClass().getSimpleName());
    		        	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		        		LayoutInflater li = LayoutInflater.from(context); 
    		        	View promptsView = li.inflate(R.layout.error_network_dialog, null);
    		        	TextView msg = (TextView) promptsView.findViewById(R.id.msg);
    		        	if (NetworkState == TkpdState.NetworkState.NO_CONNECTION) msg.setText(R.string.msg_no_connection);
    		        	else msg.setText(R.string.msg_connection_problem);
    		        	myAlertDialog.setView(promptsView);
    		     	    myAlertDialog.setPositiveButton(context.getString(R.string.title_try_again), new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								RequestManager.setDialogStats(false);
								//for (int i = 0; i < RequestManager.getRequestList().size(); i++) {
									//RequestManager.getRequestQueue().add(RequestManager.getRequestList().get(i));
								//}
								RequestManager.retryRequestList(context.getClass().getSimpleName());
								RequestManager.clearRequestList(context.getClass().getSimpleName());
								
							}  		     	    	
    		     	    });
    		     	  myAlertDialog.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							RequestManager.setDialogStats(false);
							//RequestManager.getRequestList().clear();
							
						}
					});
    		     	   RequestManager.setDialogStats(true);
    		     	   myAlertDialog.show();
    		     	   //myAlertDialog.setCancelable(true);
        			 }
        		 }
        	 } else if (!Valid) {
        		 netwrokListener.onSuccess(false);
        	 }
    	 } catch (Exception e) {
    		 netwrokListener.onSuccess(false);
    	 }

	}

	public class CustomStringRequest extends StringRequest {

		 private Priority priority = Priority.HIGH;
		
		  CustomStringRequest(int post, String url, Listener<String> listener,ErrorListener errorListener) { 
			  super(post, url, listener, errorListener);
			  /*setRetryPolicy(new  DefaultRetryPolicy(
					  Timeout, 
		              DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
		              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
			  retryVar = new RetryPolicyVar(Timeout, MaxRetries, BackoffMulti);
			  
			 setRetryPolicy(new  DefaultRetryPolicy(
					  Timeout, 
		              MaxRetries,
		              BackoffMulti));
			  
			  CommonUtils.dumper("Timeout: " + Timeout + " Max Retries: " + MaxRetries + " Multi: " + BackoffMulti);
			  
		  }
		
		  @Override
		  public Priority getPriority(){
			  return priority;
		 }
		
		 public void setPriority(Priority priority){
			 this.priority = priority;
		 }
	}
	
	public class RetryPolicyVar {
		public int Timeoutc;
		public int MaxRetriesc;
		public float BackoffMultic;
		
		public RetryPolicyVar (int var1, int var2, float var3) {
			this.Timeoutc = var1;
			this.MaxRetriesc = var2;
			this.BackoffMultic = var3;
		}
	}
	
//	private void ClearDataDialog(){
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setMessage(context.getString(R.string.error_token_expired));
//		builder.setPositiveButton(context.getString(R.string.title_yes), new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				clearApplicationData();
//			}
//		});
//		builder.setNegativeButton(context.getString(R.string.title_no), new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				
//			}
//		});
//		builder.setCancelable(true);
//		
//		Dialog dialog = builder.create();
//		dialog.show();
//	}
//	
//	 private void clearApplicationData() {
//	        File cache = context.getCacheDir();
//	        File appDir = new File(cache.getParent());
//	        if (appDir.exists()) {
//	            String[] children = appDir.list();
//	            for (String s : children) {
//	                if (!s.equals("lib")) {
//	                    deleteDir(new File(appDir, s));
//	                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
//	                }
//	            }
//	        }
//	        Toast.makeText(context, context.getString(R.string.title_application_cleared), Toast.LENGTH_SHORT).show();
////	        Intent intent = new Intent(context, SplashScreen.class);
////	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////	        context.startActivity(intent);
//	    }
//
//	    private static boolean deleteDir(File dir) {
//	        if (dir != null && dir.isDirectory()) {
//	            String[] children = dir.list();
//	            for (int i = 0; i < children.length; i++) {
//	                boolean success = deleteDir(new File(dir, children[i]));
//	                if (!success) {
//	                    return false;
//	                }
//	            }
//	        }
//
//	        return dir.delete();
//	    }

}
