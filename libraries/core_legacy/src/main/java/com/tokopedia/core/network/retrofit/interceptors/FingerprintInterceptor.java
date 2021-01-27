package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricoharisin on 3/10/17.
 */

public class FingerprintInterceptor implements Interceptor {

    public static final String ADVERTISINGID = "ADVERTISINGID";
    public static final String KEY_ADVERTISINGID = "KEY_ADVERTISINGID";
    private static final String KEY_SESSION_ID = "Tkpd-SessionId";
    private static final String KEY_USER_ID = "Tkpd-UserId";
    private static final String KEY_ACC_AUTH = "Accounts-Authorization";
    private static final String KEY_FINGERPRINT_DATA = "Fingerprint-Data";
    private static final String KEY_FINGERPRINT_HASH = "Fingerprint-Hash";
    private static final String BEARER = "Bearer ";
    private static final String KEY_ADSID = "X-GA-ID";
    private final Context context;
    private final UserSessionInterface userSession;

    @Inject
    public FingerprintInterceptor(Context context) {
        this.context = context;
        userSession = new UserSession(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addFingerPrint(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addFingerPrint(final Request.Builder newRequest) {
        FingerprintModel fpModel;
        Context appContext = context.getApplicationContext();
        if (appContext instanceof NetworkRouter) {
            fpModel = ((NetworkRouter)appContext).getFingerprintModel();
        } else {
            return newRequest;
        }
        String json64 = fpModel.getFingerprintHash();

        newRequest.addHeader(KEY_SESSION_ID, fpModel.getRegistrarionId());
        if (userSession.isLoggedIn()) {
            newRequest.addHeader(KEY_USER_ID, userSession.getUserId());
            newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json64 + "+" + userSession.getUserId()));
        } else {
            newRequest.addHeader(KEY_USER_ID, "0");
            newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json64 + "+" + "0"));
        }
        newRequest.addHeader(KEY_ACC_AUTH, BEARER + userSession.getAccessToken());
        newRequest.addHeader(KEY_FINGERPRINT_DATA, json64);
        newRequest.addHeader(KEY_ADSID, fpModel.getAdsId());
        return newRequest;
    }

}
