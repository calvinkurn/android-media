package com.tokopedia.tkpdreactnative.react.common.data.interceptor;


import android.content.Context;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.authentication.AuthKey;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricoharisin
 * <p/>
 * use this for API ONLY not WS V4
 * <p/>
 * for WS V4 use TkpdAuthInterceptor
 *
 */
// TODO remove this class from react_native
public class DynamicTkpdAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = DynamicTkpdAuthInterceptor.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String BEARER = "Bearer ";
    private String authKey = AuthKey.KEY_WSV4;

    public DynamicTkpdAuthInterceptor(Context context, NetworkRouter networkRouter, UserSessionInterface userSession) {
        super(context, networkRouter, userSession);
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        Request finalRequest = newRequest.build();

        Response response = getResponse(chain, finalRequest);

        checkResponse(response);

        return response;
    }

    @Override
    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        authKey = getAuthKey(originRequest.url().toString());
        Map<String, String> Headers = getHeaders(originRequest, authKey);
        for (Map.Entry<String, String> entry : Headers.entrySet()) {
            newRequest.addHeader(entry.getKey(), entry.getValue());
        }

        newRequest.method(originRequest.method(), originRequest.body());
    }

    private Map<String, String> getHeaders(Request originRequest, String authKey) {
        Map<String, String> Headers = AuthHelper.generateHeaders(originRequest.url().uri()
                .getPath(), "", originRequest.method(), authKey, CONTENT_TYPE_JSON, userSession.getUserId(), userSession);
        if (originRequest.url().toString().contains("/o2o/")) {
            Headers.put(AUTHORIZATION, BEARER + userSession.getAccessToken());
        }
        return Headers;
    }

    private String getAuthKey(String url) {
        if (url.contains("mojito")) {
            return AuthKey.KEY_MOJITO;
        }
        return AuthKey.KEY_WSV4;
    }


}
