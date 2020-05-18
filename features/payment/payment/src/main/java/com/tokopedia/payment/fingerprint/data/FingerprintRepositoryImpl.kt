package com.tokopedia.payment.fingerprint.data

import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint
import com.tokopedia.payment.fingerprint.domain.FingerprintRepository
import rx.Observable
import java.util.*

/**
 * Created by zulfikarrahman on 3/23/18.
 */
class FingerprintRepositoryImpl(private val fingerprintDataSourceCloud: FingerprintDataSourceCloud) : FingerprintRepository {

    override fun saveFingerprint(params: HashMap<String, Any?>): Observable<Boolean> {
        return fingerprintDataSourceCloud.saveFingerPrint(params)
    }

    override fun paymentWithFingerPrint(params: HashMap<String, Any?>): Observable<ResponsePaymentFingerprint?> {
        return fingerprintDataSourceCloud.paymentWithFingerPrint(params)
    }

    override fun savePublicKey(params: HashMap<String, String?>): Observable<Boolean> {
        return fingerprintDataSourceCloud.savePublicKey(params)
    }

    override fun getPostDataOtp(transactionId: String): Observable<HashMap<String, String>?> {
        return fingerprintDataSourceCloud.getDataPostOtp(transactionId)
    }
}