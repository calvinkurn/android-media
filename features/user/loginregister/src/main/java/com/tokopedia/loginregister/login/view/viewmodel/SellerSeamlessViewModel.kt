package com.tokopedia.loginregister.login.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seamless_login.utils.AESUtils
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 20/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SellerSeamlessViewModel @Inject constructor(@Named(SessionModule.SESSION_MODULE)
                                                  private val userSession: UserSessionInterface,
                                                  private val loginTokenUseCase: LoginTokenUseCase,
                                                  dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    private val mutableLoginTokenResponse = MutableLiveData<Result<LoginToken>>()
    val loginTokenResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginTokenResponse

    private val mutableGoToSecurityQuestion = MutableLiveData<Boolean>()
    val goToSecurityQuestion: LiveData<Boolean>
        get() = mutableGoToSecurityQuestion


    fun loginSeamless(code: String) {
        val encryptedCode = AESUtils.encryptSeamless(code.toByteArray())
        if(encryptedCode.isNotEmpty()) {
            loginTokenUseCase.executeLoginTokenSeamless(LoginTokenUseCase.generateParamLoginSeamless(encryptedCode),
                    LoginTokenSubscriber(
                            userSession,
                            onSuccessLoginToken(),
                            onFailedLoginToken(), {},
                            onGoToSecurityQuestion()
                    )
            )
        } else onFailedLoginToken()
    }

    private fun onFailedLoginToken(): (Throwable) -> Unit {
        return {
            userSession.clearToken()
            mutableLoginTokenResponse.value = Fail(it)
        }
    }
    private fun onGoToSecurityQuestion(): () -> Unit {
        return {
            mutableGoToSecurityQuestion.value = true
        }
    }

    private fun onSuccessLoginToken(): (LoginTokenPojo) -> Unit {
        return {
            if (it.loginToken.accessToken.isNotEmpty() &&
                    it.loginToken.refreshToken.isNotEmpty() &&
                    it.loginToken.tokenType.isNotEmpty()) {
                mutableLoginTokenResponse.value = Success(it.loginToken)
            } else if (it.loginToken.errors.isNotEmpty() &&
                    it.loginToken.errors[0].message.isNotEmpty()) {
                mutableLoginTokenResponse.value = Fail(MessageErrorException(it.loginToken.errors[0].message))
            } else {
                mutableLoginTokenResponse.value = Fail(RuntimeException())
            }
        }
    }

}