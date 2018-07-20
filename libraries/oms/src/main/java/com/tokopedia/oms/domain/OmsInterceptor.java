package com.tokopedia.oms.domain;

import android.content.Context;
import android.util.Log;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.user.session.UserSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class OmsInterceptor extends TkpdAuthInterceptor {
    private Context mContext;

    public OmsInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession) {
        super(context, networkRouter, userSession);
        this.mContext=context;
    }

    public OmsInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
        this.mContext=context;
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String bodyResponse = response.body().string();
        int code = response.code();
        Log.d("OkHttp", bodyResponse);

        switch (code) {
            default:
                try {
                    response.body().close();
                    JSONObject jsonResponse = new JSONObject(bodyResponse);
                    String JSON_ERROR_KEY = "message_error";
                    if (jsonResponse.has(JSON_ERROR_KEY)) {
                        JSONArray messageErrorArray = jsonResponse.optJSONArray(JSON_ERROR_KEY);
                        if (messageErrorArray != null) {
                            String message = "";
                            for (int index = 0; index < messageErrorArray.length(); index++) {
                                if (index > 0) {
                                    message += ", ";
                                }
                                message = message + messageErrorArray.getString(index);
                            }
                            handleError(bodyResponse, message);
                        } else {
                            handleError(bodyResponse, jsonResponse.getString(JSON_ERROR_KEY));
                        }
                    }
                } catch (JSONException e) {
                    handleError(null, null);
                }
        }
    }
    private void handleError(String bodyResponse, String errorMessage) throws IOException {
        ((OmsModuleRouter) mContext).handleOmsPromoError(bodyResponse, errorMessage);
    }


}