package com.tokopedia.payment.fingerprint.data

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.payment.fingerprint.data.model.DataResponseSavePublicKey
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import java.util.*

/**
 * Created by zulfikarrahman on 3/27/18.
 */
interface AccountFingerprintApi {

    @FormUrlEncoded
    @POST(PaymentFingerprintConstant.OTP_FINGERPRINT_ADD)
    fun savePublicKey(@FieldMap params: HashMap<String, String?>): Observable<Response<DataResponse<DataResponseSavePublicKey>>>
}