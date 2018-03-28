package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.domain.FingerPrintRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class PaymentFingerprintUseCase extends UseCase<ResponsePaymentFingerprint> {

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String PARTNER = "partner";
    public static final String PUBLIC_KEY = "public_key";
    public static final String DATE = "date";
    public static final String ACCOUNT_SIGNATURE = "account_signature";
    public static final String USER_ID = "user_id";
    public static final String OS = "os";
    public static final String OS_ANDROID_VALUE = "1";
    private FingerprintRepository fingerprintRepository;

    public PaymentFingerprintUseCase(FingerprintRepository fingerprintRepository) {
        this.fingerprintRepository = fingerprintRepository;
    }

    @Override
    public Observable<ResponsePaymentFingerprint> createObservable(RequestParams requestParams) {
        return fingerprintRepository.paymentWithFingerPrint(requestParams.getParamsAllValueInString());
    }

    public RequestParams createRequestParams(String transactionId, String partner, String publicKey, String date, String accountSignature, String userId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TRANSACTION_ID, transactionId);
        requestParams.putString(PARTNER, partner);
        requestParams.putString(PUBLIC_KEY, publicKey);
        requestParams.putString(DATE, date);
        requestParams.putString(ACCOUNT_SIGNATURE, accountSignature);
        requestParams.putString(USER_ID, userId);
        requestParams.putString(OS, OS_ANDROID_VALUE);
        return requestParams;
    }
}
