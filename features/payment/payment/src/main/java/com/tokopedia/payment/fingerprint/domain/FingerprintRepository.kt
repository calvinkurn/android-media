package com.tokopedia.payment.fingerprint.domain

import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint
import rx.Observable
import java.util.*

/**
 * Created by zulfikarrahman on 3/23/18.
 */
interface FingerprintRepository {
    fun saveFingerprint(params: HashMap<String, Any?>): Observable<Boolean>
    fun paymentWithFingerPrint(params: HashMap<String, Any?>): Observable<ResponsePaymentFingerprint?>
    fun savePublicKey(params: HashMap<String, String?>): Observable<Boolean>
    fun getPostDataOtp(transactionId: String): Observable<HashMap<String, String>?>
}