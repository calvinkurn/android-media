package com.tokopedia.payment.fingerprint.data;

import com.google.gson.JsonElement;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.data.model.ResponseRegisterFingerprint;
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public interface FingerprintApi {

    @FormUrlEncoded
    @POST(PaymentFingerprintConstant.V2_FINGERPRINT_PUBLICKEY_SAVE)
    Observable<Response<ResponseRegisterFingerprint>> saveFingerPrint(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(PaymentFingerprintConstant.V2_PAYMENT_CC_FINGERPRINT)
    Observable<Response<ResponsePaymentFingerprint>> paymentWithFingerPrint(@FieldMap HashMap<String, Object> params);

    @Headers({"Content-Type: application/json"})
    @POST(PaymentFingerprintConstant.V2_PAYMENT_GET_POST_DATA)
    Observable<Response<JsonElement>> getPostDataOtp(@Body HashMap<String, String> transactionId);
}
