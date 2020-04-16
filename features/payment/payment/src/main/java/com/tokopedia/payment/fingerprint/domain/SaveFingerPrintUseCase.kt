package com.tokopedia.payment.fingerprint.domain

import com.tokopedia.authentication.AuthHelper
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
        val requestParams = RequestParams.create()
        requestParams.putString(TRANSACTION_ID, transactionId)
        requestParams.putString(PUBLIC_KEY, publicKey)
        requestParams.putString(DATE, date)
        requestParams.putString(ACCOUNT_SIGNATURE, accountSignature)
        requestParams.putString(USER_ID, userId)
        requestParams.putString(OS, OS_ANDROID_VALUE)
        return requestParams
    }

    companion object {
        private const val TRANSACTION_ID = "transaction_id"
        private const val PUBLIC_KEY = "public_key"
        private const val DATE = "date"
        private const val ACCOUNT_SIGNATURE = "account_signature"
        const val USER_ID = "user_id"
        const val OS = "os"
        private const val OS_ANDROID_VALUE = "1"
    }

}