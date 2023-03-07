package com.tokopedia.payment.fingerprint.domain

import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 3/23/18.
 */
class PaymentFingerprintUseCase @Inject constructor(private val fingerprintRepository: FingerprintRepository,
                                                    private val userSession: UserSessionInterface) : UseCase<ResponsePaymentFingerprint?>() {

    override fun createObservable(requestParams: RequestParams): Observable<ResponsePaymentFingerprint?> {
        val params = AuthHelper.generateParamsNetwork(userSession.userId, userSession.deviceId, TKPDMapParam())
        requestParams.putAllString(params)
        return fingerprintRepository.getPostDataOtp(requestParams.getString(TRANSACTION_ID, ""))
                .flatMap { stringStringHashMap: HashMap<String, String>? ->
                    val params1 = requestParams.parameters
                    params1.remove(TRANSACTION_ID)
                    stringStringHashMap?.let {
                        params1.putAll(stringStringHashMap)
                    }
                    fingerprintRepository.paymentWithFingerPrint(params1)
                }
    }

    fun createRequestParams(
            transactionId: String?, publicKey: String?, date: String?, accountSignature: String?, userId: String?
    ): RequestParams {
        return RequestParams.create().apply {
            putString(TRANSACTION_ID, transactionId)
            putString(PUBLIC_KEY, publicKey)
            putString(DATE, date)
            putString(ACCOUNT_SIGNATURE, accountSignature)
            putInt(USER_ID, userId?.toIntOrNull() ?: 0)
            putString(OS, OS_ANDROID_VALUE)
        }
    }

    companion object {
        private const val TRANSACTION_ID = "transaction_id"
        private const val PUBLIC_KEY = "public_key"
        private const val DATE = "date"
        private const val ACCOUNT_SIGNATURE = "account_signature"
        private const val USER_ID = "user_id"
        private const val OS = "os"
        private const val OS_ANDROID_VALUE = "1"
    }
}