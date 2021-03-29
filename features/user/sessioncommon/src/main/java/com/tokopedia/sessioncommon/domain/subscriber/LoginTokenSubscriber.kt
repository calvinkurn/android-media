package com.tokopedia.sessioncommon.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

/**
 * @author by nisie on 11/06/19.
 */
class LoginTokenSubscriber(val userSession: UserSessionInterface,
                           val onSuccessLoginToken: (pojo: LoginTokenPojo) -> Unit,
                           val onErrorLoginToken: (e: Throwable) -> Unit,
                           val onShowPopupError: (pojo: LoginTokenPojo)  -> Unit,
                           val onGoToActivationPage: (errorMessage: MessageErrorException) -> Unit,
                           val onGoToSecurityQuestion: () -> Unit,
                           val onFinished: () -> Unit? = {}) :
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
        } else if (shouldGoToActivationPage(pojo)) {
            onGoToActivationPage(MessageErrorException(pojo.loginToken.errors[0].message))
        } else {
            removeUserSessionToken()
            if (pojo.loginToken.popupError.header.isNotEmpty() &&
                    pojo.loginToken.popupError.body.isNotEmpty() &&
                    pojo.loginToken.popupError.action.isNotEmpty()) {
                onShowPopupError(pojo)
            } else if (pojo.loginToken.errors.isNotEmpty()) {
                onErrorLoginToken(MessageErrorException(pojo.loginToken.errors[0].message))
            } else if (errors.isNotEmpty()) {
                onErrorLoginToken(MessageErrorException(errors[0].message))
            } else {
                onErrorLoginToken(Throwable())
            }
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
                    EncoderDecoder.Encrypt(refreshToken, userSession.refreshTokenIV))
        }
    }

    override fun onCompleted() {
        onFinished.invoke()
    }

    override fun onError(e: Throwable?) {
        e?.run {
            onErrorLoginToken(this)
        }
        onFinished.invoke()
    }

    private fun removeUserSessionToken() {
        userSession.setToken(null, null, null)
    }
}