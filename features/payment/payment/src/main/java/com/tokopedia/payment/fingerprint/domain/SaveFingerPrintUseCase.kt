package com.tokopedia.payment.fingerprint.domain

import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 3/22/18.
 */
class SaveFingerPrintUseCase @Inject constructor(private val fingerprintRepository: FingerprintRepository,
                                                 private val savePublicKeyUseCase: SavePublicKeyUseCase,
                                                 private val userSession: UserSessionInterface) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val params = AuthHelper.generateParamsNetwork(
                userSession.userId,
                userSession.deviceId,
                TKPDMapParam())
        requestParams.putAllString(params)
        return savePublicKeyUseCase.createObservable(savePublicKeyUseCase.createRequestParams(
                requestParams.getString(USER_ID, ""),
                requestParams.getString(PUBLIC_KEY, ""))
        ).flatMap { fingerprintRepository.saveFingerprint(requestParams.parameters) }
    }

    fun createRequestParams(transactionId: String?, publicKey: String?, date: String?, accountSignature: String?, userId: String?): RequestParams {
        return RequestParams.create().apply {
            putString(TRANSACTION_ID, transactionId)
            putString(PUBLIC_KEY, publicKey)
            putString(DATE, date)
            putString(ACCOUNT_SIGNATURE, accountSignature)
            putString(USER_ID, userId)
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