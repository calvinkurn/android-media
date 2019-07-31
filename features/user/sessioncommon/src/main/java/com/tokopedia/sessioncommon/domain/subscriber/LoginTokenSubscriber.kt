package com.tokopedia.sessioncommon.domain.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

/**
 * @author by nisie on 11/06/19.
 */
class LoginTokenSubscriber(val userSession: UserSessionInterface,
                           val onSuccessLoginToken: (pojo: LoginTokenPojo) -> Unit,
                           val onErrorLoginToken: (e: Throwable) -> Unit,
                           val onGoToActivationPage : (errorMessage : MessageErrorException) -> Unit,
                           val onGoToSecurityQuestion : () -> Unit) :
        Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        val pojo = response.getData<LoginTokenPojo>(LoginTokenPojo::class.java)
        val errors = response.getError(LoginTokenPojo::class.java)

        if (pojo.loginToken.errors.isEmpty()
                && pojo.loginToken.accessToken.isNotBlank()) {
            saveAccessToken(pojo)
            if(pojo.loginToken.sqCheck){
                onGoToSecurityQuestion()
            }else {
                onSuccessLoginToken(pojo)
            }
        } else if (shouldGoToActivationPage(pojo)) {
            onGoToActivationPage(MessageErrorException(pojo.loginToken.errors[0].message))
        }else if (pojo.loginToken.errors.isNotEmpty()) {
            onErrorLoginToken(MessageErrorException(pojo.loginToken.errors[0].message,
                    ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
        } else if (errors.isNotEmpty()) {
            onErrorLoginToken(MessageErrorException(errors[0].message,
                    ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
        } else {
            onErrorLoginToken(Throwable())
        }
    }

    private fun shouldGoToActivationPage(pojo: LoginTokenPojo): Boolean {
        val NOT_ACTIVATED = "belum diaktivasi"
        return pojo.loginToken.errors.isNotEmpty() && pojo.loginToken.errors[0].message.contains(NOT_ACTIVATED)
    }

    private fun saveAccessToken(pojo: LoginTokenPojo?) {
        pojo?.loginToken?.run {
            userSession.setToken(
                    accessToken,
                    tokenType,
                    EncoderDecoder.Encrypt(accessToken, userSession.refreshTokenIV))
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.run {
            onErrorLoginToken(this)
        }
    }


}