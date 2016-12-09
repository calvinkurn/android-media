package com.tokopedia.core.network.v4;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import com.tkpd.library.kirisame.network.entity.NetError;
import com.tkpd.library.kirisame.network.entity.VolleyNetwork;

/**
 * Created by Tkpd_Eka on 8/25/2015.
 */
@Deprecated
public class SimpleNetworkHandler extends VolleyNetwork{

    public interface SimpleNetworkHandlerListener{
        void onResult(JSONObject result);
        void onRequestFailed(NetError error, int errorCode);
    }

    public static final String POST = "POST";
    public static final String GET = "GET";

    public static final String TYPE_URLENCODED = "application/x-www-form-urlencoded";
    public static final String TYPE_TEXT = "text/html";

    private String contentType = TYPE_TEXT;

    private SimpleNetworkHandlerListener onSuccessListener;

    public SimpleNetworkHandler(Context context, String url) {
        super(context, url);
    }

    @Override
    public void onRequestResponse(String s) {
        System.out.println("KIRISAME RESULT " + s);
        try{
            onSuccessListener.onResult(new JSONObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(NetError netError, int i) {
        onSuccessListener.onRequestFailed(netError, i);
    }

    public void setOnSuccessListener(SimpleNetworkHandlerListener listener){
        onSuccessListener = listener;
    }

}
