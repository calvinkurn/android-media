package com.tokopedia.core.network.retrofit.interceptors;

import com.google.android.gms.auth.api.Auth;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

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
    private String AuthKey = AuthUtil.KEY.KEY_WSV4;


    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        Request finalRequest = newRequest.build();
        return getResponse(chain, finalRequest);

    }

    @Override
    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        AuthKey = getAuthKey(originRequest.url().toString());
        Map<String, String> Headers = AuthUtil.generateHeaders(originRequest.url().uri()
                .getPath(), originRequest.method(), AuthKey);
        for (Map.Entry<String, String> entry : Headers.entrySet()) {
            newRequest.addHeader(entry.getKey(), entry.getValue());
        }

        newRequest.method(originRequest.method(), originRequest.body());
    }

    private String getAuthKey(String url) {
        if (url.contains("mojito")) {
            return AuthUtil.KEY.KEY_MOJITO;
        }
        return AuthUtil.KEY.KEY_WSV4;
    }


}
