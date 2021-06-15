package com.tokopedia.loginphone.chooseaccount.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

class LoginFacebookSubscriber(val userSession: UserSessionInterface,
                              val onSuccessLoginToken: (pojo: LoginTokenPojo) -> Unit,
                              val onErrorLoginToken: (e: Throwable) -> Unit,
                              val onGoToSecurityQuestion: () -> Unit) :
        Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        val pojo = response.getData<LoginTokenPojo>(LoginTokenPojo::class.java)
        val errors = response.getError(LoginTokenPojo::class.java)

        if (pojo.loginToken.errors.isEmpty()
                && pojo.loginToken.accessToken.isNotBlank()) {
            saveAccessToken(pojo)
            if (pojo.loginToken.sqCheck) {
                onGoToSecurityQuestion()
            } else {
                onSuccessLoginToken(pojo)
            }
        } else if (pojo.loginToken.errors.isNotEmpty()) {
            onErrorLoginToken(MessageErrorException(pojo.loginToken.errors[0].message,
                    ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
        } else if (errors.isNotEmpty()) {
            onErrorLoginToken(MessageErrorException(errors[0].message,
                    ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
        } else {
            onErrorLoginToken(Throwable())
        }
    }

    private fun saveAccessToken(pojo: LoginTokenPojo?) {
        pojo?.loginToken?.run {
            userSession.setToken(
                    accessToken,
                    tokenType,
                    EncoderDecoder.Encrypt(refreshToken, userSession.refreshTokenIV))
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