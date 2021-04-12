package com.tokopedia.sessioncommon.domain.mapper

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 17/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class LoginV2Mapper(val userSession: UserSessionInterface) {

    fun map(loginTokenPojo: LoginTokenPojoV2,
            onSuccessLoginToken: (loginTokenPojo: LoginTokenPojoV2) -> Unit,
            onErrorLoginToken: (e: Throwable) -> Unit,
            onShowPopupError: (loginTokenPojo: LoginTokenPojoV2)  -> Unit,
            onGoToActivationPage: (errorMessage: MessageErrorException) -> Unit,
            onGoToSecurityQuestion: () -> Unit){

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
        } else {
            removeUserSessionToken()
            if (loginTokenPojo.loginToken.popupError.header.isNotEmpty() &&
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

    }

    private fun shouldGoToActivationPage(loginTokenPojo: LoginTokenPojoV2): Boolean {
        val NOT_ACTIVATED = "belum diaktivasi"
        return loginTokenPojo.loginToken.errors.isNotEmpty() && loginTokenPojo.loginToken.errors[0].message.contains(NOT_ACTIVATED)
    }

    private fun saveAccessToken(loginTokenPojo: LoginTokenPojoV2?) {
        loginTokenPojo?.loginToken?.run {
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