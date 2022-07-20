package com.tokopedia.core.network.retrofit.interceptors;


import com.tokopedia.network.authentication.AuthConstant;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.exception.SessionExpiredException;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by kris on 1/11/17. Tokopedia
 */

public class StandardizedInterceptor extends TkpdBaseInterceptor {

    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    private String authorizationString;

    public StandardizedInterceptor(String authorizationString) {
        this.authorizationString = authorizationString;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder newRequest = getBearerHeaderBuilder(chain.request(), authorizationString);

        final Request finalRequest = newRequest.build();
        Response response = getResponse(chain, finalRequest);

        String bodyResponse = response.body().string();
        try {
            JSONObject jsonResponse = new JSONObject(bodyResponse);
            String JSON_ERROR_KEY = "error";
            if(jsonResponse.has(JSON_ERROR_KEY)) {
                handleError(jsonResponse.getString(JSON_ERROR_KEY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createNewResponse(response, bodyResponse);
    }

    private void handleError(String errorMessage) throws SessionExpiredException {
        if(errorMessage.equals("invalid_request") || errorMessage.equals("invalid_grant"))
            throw new SessionExpiredException(errorMessage);
    }

    private Response createNewResponse(Response oldResponse, String oldBodyResponse) {
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

    private Request.Builder getBearerHeaderBuilder(Request request, String oAuth) {
        return request.newBuilder()
                .header("Authorization", oAuth)
                .header("Accounts-Authorization", oAuth)
                .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header(HEADER_X_APP_VERSION, "android-" + String.valueOf(GlobalConfig.VERSION_NAME))
                .header(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
                .method(request.method(), request.body());
    }

}
