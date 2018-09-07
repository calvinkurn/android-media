package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public interface FingerprintRepository {
    Observable<Boolean> saveFingerprint(HashMap<String, Object> params);

    Observable<ResponsePaymentFingerprint> paymentWithFingerPrint(HashMap<String, Object> params);

    Observable<Boolean> savePublicKey(HashMap<String, String> params);

    Observable<HashMap<String, String>> getPostDataOtp(String transactionId);
}
