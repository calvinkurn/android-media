package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by sandeepgoyal on 02/01/18.
 */

public class CampaignInterceptor extends TkpdBaseInterceptor {

    @Override
    protected Response getResponse(Chain chain, Request request) throws IOException {
        Response response = super.getResponse(chain, request);
        if (!response.isSuccessful()) {
            throw new HttpErrorException(response.code());
        }

        String bodyResponse = response.body().string();
        checkError(response,bodyResponse);
        return createNewResponse(response, bodyResponse);
    }


    protected void checkError(Response response,String responseString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseString);
            if (jsonObject.has("message_error")) {
                String newResponseString = jsonObject.getString("message_error");
                new ServerErrorException(newResponseString,newResponseString,response.code(), response.request().url().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    protected Response createNewResponse(Response response, String responseString) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            {
                if (jsonObject.has("data")) {
                    String newResponseString = jsonObject.getString("data");
                    return constructNewResponse(response, newResponseString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return response;
        }
        return response;
    }

    protected Response constructNewResponse(Response oldResponse, String oldBodyResponse) {
        ResponseBody body = ResponseBody.create(oldResponse.body().contentType(), oldBodyResponse);

        Response.Builder builder = new Response.Builder();
        builder.body(body)
                .headers(oldResponse.headers())
                .message(oldResponse.message())
                .handshake(oldResponse.handshake())
                .protocol(oldResponse.protocol())
                .cacheResponse(oldResponse.cacheResponse())
                .priorResponse(oldResponse.priorResponse())
                .code(oldResponse.code())
                .request(oldResponse.request())
                .networkResponse(oldResponse.networkResponse());

        return builder.build();
    }


}
