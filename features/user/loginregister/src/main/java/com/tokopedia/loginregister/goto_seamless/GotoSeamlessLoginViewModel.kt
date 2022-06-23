package com.tokopedia.loginregister.goto_seamless

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.goto_seamless.model.GojekProfileData
import com.tokopedia.loginregister.goto_seamless.model.LoginSeamlessParams
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase
import com.tokopedia.loginregister.goto_seamless.usecase.LoginSeamlessUseCase
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

class GotoSeamlessLoginViewModel @Inject constructor(
    private val getTemporaryKeyUseCase: GetTemporaryKeyUseCase,
    private val loginSeamlessUseCase: LoginSeamlessUseCase,
    private val gotoSeamlessHelper: GotoSeamlessHelper,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val mutableLoginResponse = MutableLiveData<Result<LoginToken>>()
    val loginResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginResponse

    private val mutableGojekProfileData = MutableLiveData<Result<GojekProfileData>>()
    val gojekProfileData: LiveData<Result<GojekProfileData>>
        get() = mutableGojekProfileData

    fun getGojekData() {
        try {
            launch {
                val result = gotoSeamlessHelper.getGojekProfile()
                mutableGojekProfileData.value = Success(result)
            }
        } catch (e: Exception) {
            mutableGojekProfileData.value = Fail(e)
        }
    }

    fun doSeamlessLogin(authCode: String) {
        try {
            launch {
                userSessionInterface.setToken(TokenGenerator().createBasicTokenGQL(), "")
                val params = LoginSeamlessParams(
                    grantType = LoginSeamlessUseCase.GRANT_TYPE_AUTH_CODE,
                    authCode = authCode
                )
                val result = loginSeamlessUseCase(params).loginToken
                onSuccessSeamlessLogin(result)
            }
        } catch (e: Exception) {
            mutableLoginResponse.value = Fail(e)
        }
    }

    fun onSuccessSeamlessLogin(data: LoginToken) {
        if(data.accessToken.isNotEmpty()) {
            saveAccessToken(data)
            mutableLoginResponse.value = Success(data)
        } else {
            mutableLoginResponse.value = Fail(Throwable("Access token is empty"))
        }
    }

    private fun saveAccessToken(loginToken: LoginToken) {
        userSessionInterface.setToken(
            loginToken.accessToken,
            loginToken.tokenType,
            EncoderDecoder.Encrypt(loginToken.refreshToken, userSessionInterface.refreshTokenIV))
    }
}