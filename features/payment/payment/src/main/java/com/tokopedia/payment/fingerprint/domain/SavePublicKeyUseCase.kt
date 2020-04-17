package com.tokopedia.payment.fingerprint.domain

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 3/27/18.
 */
class SavePublicKeyUseCase @Inject constructor(private val fingerprintRepository: FingerprintRepository,
                                               private val userSession: UserSessionInterface) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val params = AuthHelper.generateParamsNetwork(userSession.userId, userSession.deviceId, TKPDMapParam())
        requestParams.putAllString(params)
        return fingerprintRepository.savePublicKey(requestParams.paramsAllValueInString)
    }

    fun createRequestParams(userId: String?, publicKey: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(USER_ID, userId)
        requestParams.putString(PUBLIC_KEY, publicKey)
        return requestParams
    }

    companion object {
        private const val USER_ID = "user_id"
        private const val PUBLIC_KEY = "public_key"
    }

}