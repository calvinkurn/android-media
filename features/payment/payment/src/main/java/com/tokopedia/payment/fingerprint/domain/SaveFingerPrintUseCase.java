package com.tokopedia.payment.fingerprint.domain;

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
    public static final String USER_ID_SAVE_PUBLIC = "user_id_save_public";
    private FingerprintRepository fingerprintRepository;
    private SavePublicKeyUseCase savePublicKeyUseCase;

    @Inject
    public SaveFingerPrintUseCase(FingerprintRepository fingerprintRepository,
                                  SavePublicKeyUseCase savePublicKeyUseCase) {
        this.fingerprintRepository = fingerprintRepository;
        this.savePublicKeyUseCase = savePublicKeyUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        return savePublicKeyUseCase.createObservable(savePublicKeyUseCase.createRequestParams(requestParams.getString(USER_ID_SAVE_PUBLIC, ""),
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
        requestParams.putInt(USER_ID, Integer.valueOf(userId));
        requestParams.putString(USER_ID_SAVE_PUBLIC, userId);
        requestParams.putString(OS, OS_ANDROID_VALUE);
        return requestParams;
    }
}
