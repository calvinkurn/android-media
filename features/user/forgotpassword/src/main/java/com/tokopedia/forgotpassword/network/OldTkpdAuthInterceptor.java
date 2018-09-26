package com.tokopedia.forgotpassword.network;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by nisie on 9/26/18.
 */
public class OldTkpdAuthInterceptor extends TkpdAuthInterceptor {

    public static final String HEADER_HMAC_SIGNATURE_KEY = "TKPDROID AndroidApps:";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String OLD_HEADER_HMAC_SIGNATURE_KEY = "TKPD Tokopedia:";


    public OldTkpdAuthInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession) {
        super(context, networkRouter, userSession);
    }

    public OldTkpdAuthInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder newRequest = chain.request().newBuilder();
        String newAuthHeader = chain.request().header(HEADER_AUTHORIZATION).replace
                (HEADER_HMAC_SIGNATURE_KEY, OLD_HEADER_HMAC_SIGNATURE_KEY);
        newRequest.removeHeader(HEADER_AUTHORIZATION);
        newRequest.addHeader(HEADER_AUTHORIZATION, newAuthHeader);

        return chain.proceed(newRequest.build());
    }
}
