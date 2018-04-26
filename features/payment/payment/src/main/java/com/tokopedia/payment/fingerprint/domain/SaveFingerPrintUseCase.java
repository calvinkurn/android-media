package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/18.
 */

public class SaveFingerPrintUseCase extends UseCase<Boolean> {

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String PUBLIC_KEY = "public_key";
    public static final String DATE = "date";
    public static final String ACCOUNT_SIGNATURE = "account_signature";
    public static final String USER_ID = "user_id";
    public static final String OS = "os";
    public static final String OS_ANDROID_VALUE = "1";
    private FingerprintRepository fingerprintRepository;
    private SavePublicKeyUseCase savePublicKeyUseCase;
    private UserSession userSession;

    @Inject
    public SaveFingerPrintUseCase(FingerprintRepository fingerprintRepository,
                                  SavePublicKeyUseCase savePublicKeyUseCase, UserSession userSession) {
        this.fingerprintRepository = fingerprintRepository;
        this.savePublicKeyUseCase = savePublicKeyUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        TKPDMapParam<String, String> params = AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), new TKPDMapParam<String, String>());
        requestParams.putAllString(params);
        return savePublicKeyUseCase.createObservable(savePublicKeyUseCase.createRequestParams(requestParams.getString(USER_ID, ""),
                requestParams.getString(PUBLIC_KEY, "")))
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return fingerprintRepository.saveFingerprint(requestParams.getParameters());
                    }
                });
    }

    public RequestParams createRequestParams(String transactionId, String publicKey, String date, String accountSignature, String userId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TRANSACTION_ID, transactionId);
        requestParams.putString(PUBLIC_KEY, publicKey);
        requestParams.putString(DATE, date);
        requestParams.putString(ACCOUNT_SIGNATURE, accountSignature);
        requestParams.putString(USER_ID, userId);
        requestParams.putString(OS, OS_ANDROID_VALUE);
        return requestParams;
    }
}
