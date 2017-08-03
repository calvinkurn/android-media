package com.tokopedia.core.network.retrofit.interceptors;

import android.util.Base64;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.fingerprint.Utilities;
import com.tokopedia.core.analytics.fingerprint.data.FingerprintDataRepository;
import com.tokopedia.core.analytics.fingerprint.domain.FingerprintRepository;
import com.tokopedia.core.analytics.fingerprint.domain.usecase.GetFingerprintUseCase;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by ricoharisin on 3/10/17.
 */

public class FingerprintInterceptor implements Interceptor {

    private static final String KEY_SESSION_ID  = "Tkpd-SessionId";
    private static final String KEY_USER_ID     = "Tkpd-UserId";
    private static final String KEY_ACC_AUTH    = "Accounts-Authorization";
    private static final String KEY_FINGERPRINT_DATA = "Fingerprint-Data";
    private static final String KEY_FINGERPRINT_HASH = "Fingerprint-Hash";
    private static final String BEARER = "Bearer ";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addFingerPrint(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addFingerPrint(final Request.Builder newRequest) {
        GetFingerprintUseCase getFingerprintUseCase;
        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        FingerprintRepository fpRepo = new FingerprintDataRepository();
        getFingerprintUseCase = new GetFingerprintUseCase(threadExecutor, postExecutionThread, fpRepo);
        String json = getFingerprintUseCase.createObservable(null)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s;
                    }
                }).map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        try {
                            return Utilities.toBase64(s, Base64.NO_WRAP);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "UnsupportedEncoding";
                        }
                    }
                }).toSingle().toBlocking().value();

        SessionHandler session = new SessionHandler(MainApplication.getAppContext());
        newRequest.addHeader(KEY_SESSION_ID, FCMCacheManager.getRegistrationIdWithTemp(MainApplication.getAppContext()));
        if (session.isV4Login()) {
            newRequest.addHeader(KEY_USER_ID, session.getLoginID());
        } else {
            newRequest.addHeader(KEY_USER_ID, "0");
        }
        newRequest.addHeader(KEY_ACC_AUTH,BEARER+session.getAccessToken(MainApplication.getAppContext()));
        newRequest.addHeader(KEY_FINGERPRINT_DATA, json);
        newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json+"+"+session.getLoginID()));

        return newRequest;
    }
}
