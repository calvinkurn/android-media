package com.tokopedia.payment.fingerprint.data

import com.google.gson.JsonElement
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint
import com.tokopedia.payment.fingerprint.data.model.ResponseRegisterFingerprint
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant
import retrofit2.Response
import retrofit2.http.*
import rx.Observable
import java.util.*

/**
 * Created by zulfikarrahman on 3/23/18.
 */
interface FingerprintApi {

    @FormUrlEncoded
    @POST(PaymentFingerprintConstant.V2_FINGERPRINT_PUBLICKEY_SAVE)
    fun saveFingerPrint(@FieldMap params: Map<String, Any?>): Observable<Response<ResponseRegisterFingerprint>>

    @FormUrlEncoded
    @POST(PaymentFingerprintConstant.V2_PAYMENT_CC_FINGERPRINT)
    fun paymentWithFingerPrint(@FieldMap params: HashMap<String, Any?>): Observable<Response<ResponsePaymentFingerprint>>

    @Headers("Content-Type: application/json")
    @POST(PaymentFingerprintConstant.V2_PAYMENT_GET_POST_DATA)
    fun getPostDataOtp(@Body transactionId: HashMap<String, String>): Observable<Response<JsonElement>>
}