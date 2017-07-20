package com.tokopedia.core.network.retrofit.interceptors;

import com.tkpd.library.utils.CommonUtils;
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

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.functions.Func1;

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
                }).toSingle().toBlocking().value();

        SessionHandler session = new SessionHandler(MainApplication.getAppContext());
        newRequest.addHeader("Tkpd-SessionId", FCMCacheManager.getRegistrationIdWithTemp(MainApplication.getAppContext()));
        if (session.isV4Login()) {
            newRequest.addHeader("Tkpd-UserId", session.getLoginID());
        }
        newRequest.addHeader("Fingerprint-Data", json);
        newRequest.addHeader("Fingerprint-Hash", AuthUtil.md5(json+"+"+session.getLoginID()));

        CommonUtils.dumper("theresult returned request "+json);
        return newRequest;
    }
}
