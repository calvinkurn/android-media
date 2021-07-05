package com.tokopedia.loginregister.login.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.common.analytics.SeamlessLoginAnalytics
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seamless_login_common.utils.AESUtils
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 20/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SellerSeamlessViewModel @Inject constructor(@Named(SessionModule.SESSION_MODULE)
                                                  private val userSession: UserSessionInterface,
                                                  private val loginTokenUseCase: LoginTokenUseCase,
                                                  private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val mutableLoginTokenResponse = MutableLiveData<Result<LoginToken>>()
    val loginTokenResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginTokenResponse

    private val mutableGoToSecurityQuestion = MutableLiveData<Boolean>()
    val goToSecurityQuestion: LiveData<Boolean>
        get() = mutableGoToSecurityQuestion

    fun hasLogin(): Boolean = userSession.email.isNotEmpty()

    fun loginSeamless(code: String) {
        val encryptedCode = AESUtils.encryptSeamless(code.toByteArray())
        if(encryptedCode.isNotEmpty()) {
            loginTokenUseCase.executeLoginTokenSeamless(LoginTokenUseCase.generateParamLoginSeamless(encryptedCode),
                    LoginTokenSubscriber(
                            userSession,
                            onSuccessLoginToken(),
                            onFailedLoginToken(),
                            onSuccessLoginToken(),
                            {},
                            onGoToSecurityQuestion()
                    )
            )
        } else {
            onFailedLoginToken().invoke(Throwable())
        }
    }

    private fun onFailedLoginToken(): (Throwable) -> Unit {
        return {
            userSession.clearToken()
            mutableLoginTokenResponse.postValue(Fail(it))
        }
    }
    private fun onGoToSecurityQuestion(): () -> Unit {
        return {
            userSession.loginMethod = SeamlessLoginAnalytics.LOGIN_METHOD_SEAMLESS
            mutableGoToSecurityQuestion.postValue(true)
        }
    }

    private fun onSuccessLoginToken(): (LoginTokenPojo) -> Unit {
        return {
            if (it.loginToken.accessToken.isNotEmpty() &&
                    it.loginToken.refreshToken.isNotEmpty() &&
                    it.loginToken.tokenType.isNotEmpty()) {
                mutableLoginTokenResponse.postValue(Success(it.loginToken))
            } else if (it.loginToken.errors.isNotEmpty() &&
                    it.loginToken.errors[0].message.isNotEmpty()) {
                mutableLoginTokenResponse.postValue(Fail(MessageErrorException(it.loginToken.errors[0].message)))
            } else {
                mutableLoginTokenResponse.postValue(Fail(RuntimeException()))
            }
        }
    }
}