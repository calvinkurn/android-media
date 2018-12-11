package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.abstraction.common.data.model.session.UserSession;

import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public class PaymentFingerprintUseCase extends UseCase<ResponsePaymentFingerprint> {

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String PUBLIC_KEY = "public_key";
    public static final String DATE = "date";
    public static final String ACCOUNT_SIGNATURE = "account_signature";
    public static final String USER_ID = "user_id";
    public static final String OS = "os";
    public static final String OS_ANDROID_VALUE = "1";
    private FingerprintRepository fingerprintRepository;
    private UserSession userSession;

    @Inject
    public PaymentFingerprintUseCase(FingerprintRepository fingerprintRepository, UserSession userSession) {
        this.fingerprintRepository = fingerprintRepository;
        this.userSession = userSession;
    }

    @Override
    public Observable<ResponsePaymentFingerprint> createObservable(final RequestParams requestParams) {
        Map<String, String> params = AuthUtil.generateParamsNetwork(
                userSession.getUserId(), userSession.getDeviceId(), new TKPDMapParam<>()
        );
        requestParams.putAllString(params);
        return fingerprintRepository.getPostDataOtp(requestParams.getString(TRANSACTION_ID, ""))
                .flatMap((Func1<HashMap<String, String>, Observable<ResponsePaymentFingerprint>>) stringStringHashMap
                        -> {
                    HashMap<String, Object> params1 = requestParams.getParameters();
                    params1.remove(TRANSACTION_ID);
                    params1.putAll(stringStringHashMap);
                    return fingerprintRepository.paymentWithFingerPrint(params1);
                });
    }

    public RequestParams createRequestParams(
            String transactionId, String publicKey, String date, String accountSignature, String userId
    ) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TRANSACTION_ID, transactionId);
        requestParams.putString(PUBLIC_KEY, publicKey);
        requestParams.putString(DATE, date);
        requestParams.putString(ACCOUNT_SIGNATURE, accountSignature);
        requestParams.putInt(USER_ID, Integer.valueOf(userId));
        requestParams.putString(OS, OS_ANDROID_VALUE);
        return requestParams;
    }
}
