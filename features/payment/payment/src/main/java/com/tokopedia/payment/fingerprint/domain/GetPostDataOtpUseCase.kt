package com.tokopedia.payment.fingerprint.domain

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 4/9/18.
 */
class GetPostDataOtpUseCase @Inject constructor(private val fingerprintRepository: FingerprintRepository) : UseCase<HashMap<String, String>?>() {

    override fun createObservable(requestParams: RequestParams): Observable<HashMap<String, String>?> {
        return fingerprintRepository.getPostDataOtp(requestParams.getString(TRANSACTION_ID, ""))
    }

    fun createRequestParams(transactionId: String): RequestParams {
        return RequestParams.create().apply {
            putObject(TRANSACTION_ID, transactionId)
        }
    }

    companion object {
        private const val TRANSACTION_ID = "transaction_id"
    }

}