package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricoharisin on 3/10/17.
 */

public class FingerprintInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addFingerPrint(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addFingerPrint(Request.Builder newRequest) {
        SessionHandler session = new SessionHandler(MainApplication.getAppContext());

        if (session.isV4Login()) {
            String fingerprint = GCMHandler.getRegistrationId(MainApplication.getAppContext()) + "~"
                    + session.getLoginID();

            newRequest.addHeader("fingerprint", fingerprint);
        }

        return newRequest;
    }
}
