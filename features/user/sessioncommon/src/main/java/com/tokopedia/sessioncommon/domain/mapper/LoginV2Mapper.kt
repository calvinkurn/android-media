package com.tokopedia.sessioncommon.domain.mapper

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 17/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class LoginV2Mapper(val userSession: UserSessionInterface) {

    fun map(loginToken: LoginToken,
                    onSuccessLoginToken: (loginTokenPojo: LoginToken) -> Unit,
                    onErrorLoginToken: (e: Throwable) -> Unit,
                    onShowPopupError: (loginTokenPojo: LoginToken)  -> Unit,
                    onGoToActivationPage: (errorMessage: MessageErrorException) -> Unit,
                    onGoToSecurityQuestion: () -> Unit) {

        val errors = loginToken.errors
        if (loginToken.errors.isEmpty()
            && loginToken.accessToken.isNotBlank()) {
            saveAccessToken(loginToken)
            if (loginToken.sqCheck) {
                onGoToSecurityQuestion()
            } else {
                onSuccessLoginToken(loginToken)
            }
        } else if (shouldGoToActivationPage(loginToken)) {
            onGoToActivationPage(MessageErrorException(loginToken.errors[0].message))
        } else {
            removeUserSessionToken()
            if (loginToken.popupError.header.isNotEmpty() &&
                loginToken.popupError.body.isNotEmpty() &&
                loginToken.popupError.action.isNotEmpty()) {
                onShowPopupError(loginToken)
            } else if (loginToken.errors.isNotEmpty()) {
                onErrorLoginToken(MessageErrorException(loginToken.errors[0].message))
            } else if (errors.isNotEmpty()) {
                onErrorLoginToken(MessageErrorException(errors[0].message))
            } else {
                onErrorLoginToken(Throwable())
            }
        }
    }

    private fun shouldGoToActivationPage(loginToken: LoginToken): Boolean {
        val NOT_ACTIVATED = "belum diaktivasi"
        return loginToken.errors.isNotEmpty() && loginToken.errors[0].message.contains(NOT_ACTIVATED)
    }

    private fun saveAccessToken(loginToken: LoginToken?) {
        loginToken?.run {
            userSession.setToken(
                    accessToken,
                    tokenType,
                    EncoderDecoder.Encrypt(refreshToken, userSession.refreshTokenIV))
        }
    }

    private fun removeUserSessionToken() {
        userSession.setToken(null, null, null)
    }
}