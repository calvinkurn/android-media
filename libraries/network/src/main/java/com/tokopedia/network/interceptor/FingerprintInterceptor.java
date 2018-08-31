package com.tokopedia.network.interceptor;

import android.net.Network;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author ricoharisin .
 */

public class FingerprintInterceptor implements Interceptor {

    private static final String KEY_SESSION_ID = "Tkpd-SessionId";
    private static final String KEY_USER_ID = "Tkpd-UserId";
    private static final String KEY_ACC_AUTH = "Accounts-Authorization";
    private static final String KEY_FINGERPRINT_DATA = "Fingerprint-Data";
    private static final String KEY_FINGERPRINT_HASH = "Fingerprint-Hash";
    private static final String BEARER = "Bearer ";
    private static final String KEY_ADSID = "X-GA-ID";

    private NetworkRouter networkRouter;
    private UserSession userSession;

    public FingerprintInterceptor (NetworkRouter networkRouter, UserSession userSession) {
        this.networkRouter = networkRouter;
        this.userSession = userSession;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addFingerPrint(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addFingerPrint(final Request.Builder newRequest) {
        FingerprintModel fingerprintModel = networkRouter.getFingerprintModel();
        String json = fingerprintModel.getFingerprintHash();
        newRequest.addHeader(KEY_SESSION_ID, fingerprintModel.getRegistrarionId());
        if (userSession.isLoggedIn()) {
            newRequest.addHeader(KEY_USER_ID, userSession.getUserId());
            newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json + "+" + userSession.getUserId()));
        } else {
            newRequest.addHeader(KEY_USER_ID, "0");
            newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json + "+" + "0"));
        }
//        newRequest.addHeader(KEY_USER_ID, userSession.getUserId());
//        newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json + "+" + userSession.getUserId()));
        newRequest.addHeader(KEY_ACC_AUTH, BEARER + userSession.getAccessToken());
        newRequest.addHeader(KEY_FINGERPRINT_DATA, json);
        newRequest.addHeader(KEY_ADSID, fingerprintModel.getAdsId());

        return newRequest;
    }
}
