package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.CoreNetworkApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

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
 */
public class DynamicTkpdAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = DynamicTkpdAuthInterceptor.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private String AuthKey = AuthUtil.KEY.KEY_WSV4;


    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        Request finalRequest = newRequest.build();

        Response response = getResponse(chain, finalRequest);

        String bodyResponse = response.body().string();
        checkResponse(bodyResponse, response);

        return createNewResponse(response, bodyResponse);
    }

    @Override
    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        AuthKey = getAuthKey(originRequest.url().toString());
        Map<String, String> Headers = getHeaders(originRequest, AuthKey);
        for (Map.Entry<String, String> entry : Headers.entrySet()) {
            newRequest.addHeader(entry.getKey(), entry.getValue());
        }

        newRequest.method(originRequest.method(), originRequest.body());
    }

    private Map<String, String> getHeaders(Request originRequest, String authKey) {
        Map<String, String> Headers = AuthUtil.generateHeaders(originRequest.url().uri()
                .getPath(), originRequest.method(), authKey);

        if (originRequest.url().toString().contains("/o2o/")) {
            UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
            Headers.put(AUTHORIZATION, BEARER + userSession.getAccessToken());
        }

        return Headers;
    }

    private String getAuthKey(String url) {
        if (url.contains("mojito")) {
            return AuthUtil.KEY.KEY_MOJITO;
        }
        return AuthUtil.KEY.KEY_WSV4;
    }


}
