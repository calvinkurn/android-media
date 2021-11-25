package com.tokopedia.oms.domain;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
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
        this.mContext = context;
    }

    public OmsInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
        this.mContext = context;
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        if (response != null) {
            String bodyResponse = response.peekBody(BYTE_COUNT).string();
            if (!TextUtils.isEmpty(bodyResponse)) {
                Log.d("OkHttp", bodyResponse);
                throw new ResponseErrorException(getErrorMessage(bodyResponse));
            }
        } else {
            throw new ResponseErrorException();
        }
    }

    public String getErrorMessage(String errorMessage) {
        JSONObject dataJsonObject = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(errorMessage);
            dataJsonObject = jsonObject.optJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String title = "";

        if (dataJsonObject != null) {
            title = dataJsonObject.optString("message");
        }
        //parse message_error array and combined all strings
        try {
            if (title != null && title.length() > 0) {
                return title;
            }

            String JSON_ERROR_KEY = "message_error";
            if (jsonObject != null && jsonObject.has(JSON_ERROR_KEY)) {
                JSONArray messageErrorArray = jsonObject.optJSONArray(JSON_ERROR_KEY);
                if (messageErrorArray != null) {
                    for (int index = 0; index < messageErrorArray.length(); index++) {
                        if (index > 0) {
                            title += ", ";
                        }

                        title = title + messageErrorArray.getString(index);
                    }
                } else {
                    title = jsonObject.getString(JSON_ERROR_KEY);
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }
}