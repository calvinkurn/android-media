package com.tokopedia.tkpd.network;

import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.network.authentication.AuthKey;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

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

    private FingerprintModel fingerprintModel;
    private UserSessionInterface userSession;

    public FingerprintInterceptor(FingerprintModel fingerprintModel, UserSession userSession) {
        this.fingerprintModel = fingerprintModel;
        this.userSession = userSession;
    }

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder newRequest = addFingerPrint(chain.request().newBuilder());
        return chain.proceed(newRequest.build());
    }

    private Request.Builder addFingerPrint(final Request.Builder newRequest) {
        String json = fingerprintModel.getFingerprintHash();
        newRequest.addHeader(KEY_SESSION_ID, fingerprintModel.getRegistrarionId());
        newRequest.addHeader(KEY_USER_ID, userSession.getUserId());
        newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthHelper.getMD5Hash(json + "+" + userSession.getUserId()));
        newRequest.addHeader(KEY_ACC_AUTH, BEARER + userSession.getAccessToken());
        newRequest.addHeader(KEY_FINGERPRINT_DATA, json);
        newRequest.addHeader(KEY_ADSID, fingerprintModel.getAdsId());
        return newRequest;
    }
}
