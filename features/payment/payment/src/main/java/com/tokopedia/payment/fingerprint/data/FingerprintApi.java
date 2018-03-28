package com.tokopedia.payment.fingerprint.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.payment.fingerprint.data.model.DataResponseSavePublicKey;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.data.model.ResponseRegisterFingerprint;
import com.tokopedia.payment.fingerprint.util.FingerprintConstant;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public interface FingerprintApi {

    @FormUrlEncoded
    @POST(FingerprintConstant.V2_FINGERPRINT_PUBLICKEY_SAVE)
    Observable<Response<ResponseRegisterFingerprint>> saveFingerPrint(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(FingerprintConstant.V2_PAYMENT_CC_FINGERPRINT)
    Observable<Response<ResponsePaymentFingerprint>> paymentWithFingerPrint(@FieldMap HashMap<String, String> params);
}
