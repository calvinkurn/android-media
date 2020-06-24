package com.tokopedia.network.interceptor;

import com.tokopedia.authentication.AuthHelper;
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
        String adsId = fingerprintModel.getAdsId();
        if(!TextUtils.isEmpty(adsId)){
            adsId = trimGoogleAdId(adsId);
        }
        newRequest.addHeader(KEY_ADSID, adsId);



        return newRequest;
    }

    static String trimGoogleAdId(String googleAdsId){
        StringBuilder sb = new StringBuilder(googleAdsId.length());//we know this is the capacity so we initialise with it:
        for (int i = 0; i < googleAdsId.length(); i++) {
            char c = googleAdsId.charAt(i);
            switch (c){
                case '\u2013':
                case '\u2014':
                case '\u2015':
                    sb.append('-');
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
