package com.tokopedia.core.network.v4;

import android.content.Context;
import android.util.Log;

import com.tkpd.library.utils.Logger;
import com.tokopedia.core2.BuildConfig;
import com.tokopedia.core.network.TkpdNetworkURLHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import com.tkpd.library.kirisame.network.entity.NetError;
import com.tkpd.library.kirisame.network.entity.VolleyNetwork;
import com.tokopedia.core.util.GlobalConfig;

/**
 * Created by ricoharisin on 8/25/15.
 */
@Deprecated
public class NetworkHandler extends VolleyNetwork {

    private static final String TAG = "NetworkHandlerV4";

    private OnNetworkResponseListener listener;
    private OnBasicResponseListener basicListener;
    private OnNetworkTimeout timeoutListener;

    public NetworkHandler(Context context, String url) {
        super(context, url);
    }

    public void setTimeoutListener(OnNetworkTimeout listener) {
        this.timeoutListener = listener;
    }

    protected void NetworkLog() {
        Log.d(TAG, "URL : " + getUrlWithQuery());
        Log.d(TAG, "Sending Param to ws: " + param.toString());
        Log.d(TAG, "Sending Header to ws: " + header.toString());
    }

    public void AddParam(String key, Object value) {
        if (value != null) addParam(key, value.toString());
    }

    public void setOnNetworkResponseListener(OnNetworkResponseListener listener) {
        this.listener = listener;
    }

    public Map<String, String> getContent(){
        return param;
    }

    public Map<String, String> getHeader(){
        return header;
    }

    @Override
    protected String getUrlWithQuery() {
        if(TkpdNetworkURLHandler.getProtocolHttp(context))
            url = url.replace("https:", "http:");
        return super.getUrlWithQuery();
    }

    public void commit() {
        AddParam("device", "android");
        AddParam("app_version", GlobalConfig.VERSION_CODE);
        AddParam("os_type", "1");
//        if (Method == VolleyNetwork.METHOD_GET) url = generateUrlWithQuery(url);
        NetworkLog();
        super.commit();
    }


    @Override
    public void onRequestResponse(String s) {
        Logger.i(TAG, "Response From: " + url);
        Logger.i(TAG, s);
        onBasicResponse(s);
        onNetworkResponse(s);
    }

    @Override
    public void onRequestError(NetError netError, int i) {
        Log.d(TAG, "NetworkError " + netError + " response code " + i);
        switch (netError) {
            case TIMEOUT:
                if (timeoutListener != null) timeoutListener.onNetworkTimeout(this);
                break;
        }
        if (listener != null)
            listener.onNetworkError(netError, i);
    }

    public void setBasicListener(OnBasicResponseListener basicListener) {
        this.basicListener = basicListener;
    }

    private void onBasicResponse(String result) {
        if (basicListener != null)
            basicListener.onResponse(result);
    }

    private void onNetworkResponse(String result) {
        if (listener == null)
            return;

        try {
            JSONObject JsonResponse = new JSONObject(result);
            Logger.i(TAG, JsonResponse.toString(1));
            JSONObject Result = JsonResponse.optJSONObject("data");
            if(Result!=null)
                listener.onResponse(Result);

            if (!JsonResponse.isNull("message_error")) {
                JSONArray JsonErrorList = new JSONArray(JsonResponse.getString("message_error"));
                ArrayList<String> ErrorList = new ArrayList<String>();
                for (int i = 0; i < JsonErrorList.length(); i++) {
                    String JsonErrorListString = JsonErrorList.getString(i);
                    Log.e(TAG, "error message read : "+JsonErrorListString);
                    ErrorList.add(JsonErrorListString);
                }
                if (ErrorList.size() > 0)
                    listener.onMessageError(ErrorList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private String generateUrlWithQuery(String url) {
//        url = url+"?";
//        int i = 0;
//        for (Map.Entry<String,String> entry : param.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            if (i == 0) url = url+key+"="+value.replace(" ", "%20");
//            else url = url+"&"+key+"="+value.replace(" ", "%20");
//
//            i++;
//        }
//        return url;
//    }


}
