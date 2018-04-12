package com.tokopedia.payment.fingerprint.data;

import com.google.gson.JsonElement;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.payment.fingerprint.data.model.DataResponseSavePublicKey;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.data.model.ResponseRegisterFingerprint;
import com.tokopedia.payment.fingerprint.util.FingerprintConstant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/23/18.
 */

public interface FingerprintApi {

    @FormUrlEncoded
    @POST(FingerprintConstant.V2_FINGERPRINT_PUBLICKEY_SAVE)
    Observable<Response<ResponseRegisterFingerprint>> saveFingerPrint(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(FingerprintConstant.V2_PAYMENT_CC_FINGERPRINT)
    Observable<Response<ResponsePaymentFingerprint>> paymentWithFingerPrint(@FieldMap HashMap<String, Object> params);

    @Headers({"Content-Type: application/json"})
    @POST(FingerprintConstant.V2_PAYMENT_GET_POST_DATA)
    Observable<Response<JsonElement>> getPostDataOtp(@Body HashMap<String, String> transactionId);
}
