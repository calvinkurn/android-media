package com.tokopedia.sessioncommon.domain.mapper

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 17/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class LoginV2Mapper(loginTokenPojo: LoginTokenPojo,
                    val userSession: UserSessionInterface,
                    onSuccessLoginToken: (loginTokenPojo: LoginTokenPojo) -> Unit,
                    onErrorLoginToken: (e: Throwable) -> Unit,
                    onShowPopupError: (loginTokenPojo: LoginTokenPojo)  -> Unit,
                    onGoToActivationPage: (errorMessage: MessageErrorException) -> Unit,
                    onGoToSecurityQuestion: () -> Unit) {
    
    init {
        val errors = loginTokenPojo.loginToken.errors
        if (loginTokenPojo.loginToken.errors.isEmpty()
                && loginTokenPojo.loginToken.accessToken.isNotBlank()) {
            saveAccessToken(loginTokenPojo)
            if (loginTokenPojo.loginToken.sqCheck) {
                onGoToSecurityQuestion()
            } else {
                onSuccessLoginToken(loginTokenPojo)
            }
        } else if (shouldGoToActivationPage(loginTokenPojo)) {
            onGoToActivationPage(MessageErrorException(loginTokenPojo.loginToken.errors[0].message))
        } else if (loginTokenPojo.loginToken.popupError.header.isNotEmpty() &&
                loginTokenPojo.loginToken.popupError.body.isNotEmpty() &&
                loginTokenPojo.loginToken.popupError.action.isNotEmpty()) {
            onShowPopupError(loginTokenPojo)
        } else if (loginTokenPojo.loginToken.errors.isNotEmpty()) {
            onErrorLoginToken(MessageErrorException(loginTokenPojo.loginToken.errors[0].message))
        } else if (errors.isNotEmpty()) {
            onErrorLoginToken(MessageErrorException(errors[0].message))
        } else {
            onErrorLoginToken(Throwable())
        }
    }

    private fun shouldGoToActivationPage(loginTokenPojo: LoginTokenPojo): Boolean {
        val NOT_ACTIVATED = "belum diaktivasi"
        return loginTokenPojo.loginToken.errors.isNotEmpty() && loginTokenPojo.loginToken.errors[0].message.contains(NOT_ACTIVATED)
    }

    private fun saveAccessToken(loginTokenPojo: LoginTokenPojo?) {
        loginTokenPojo?.loginToken?.run {
            userSession.setToken(
                    accessToken,
                    tokenType,
                    EncoderDecoder.Encrypt(refreshToken, userSession.refreshTokenIV))
        }
    }
}