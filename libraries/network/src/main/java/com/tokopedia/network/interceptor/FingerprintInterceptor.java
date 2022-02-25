package com.tokopedia.network.interceptor;

import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.user.session.UserSessionInterface;

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
    public static final String TYPING_VELOCITY = "typing_velocity";

    private NetworkRouter networkRouter;
    private UserSessionInterface userSession;

    public FingerprintInterceptor(NetworkRouter networkRouter, UserSessionInterface userSession) {
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
        newRequest.addHeader(KEY_USER_ID, userSession.getUserId());
        newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthHelper.Companion.getMD5Hash(json + "+" + userSession.getUserId()));
        newRequest.removeHeader(KEY_ACC_AUTH); //prevent double
        newRequest.addHeader(KEY_ACC_AUTH, String.format("%s %s", userSession.getTokenType(),
                userSession.getAccessToken()));
        newRequest.addHeader(KEY_FINGERPRINT_DATA, json);
        newRequest.addHeader(KEY_ADSID, fingerprintModel.getAdsId());
        return newRequest;
    }
}
