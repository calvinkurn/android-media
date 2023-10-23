package com.scp.auth.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scp.auth.GotoSdk
import com.scp.auth.common.analytics.AuthAnalyticsMapper
import com.scp.auth.common.utils.ScpUtils
import com.scp.login.core.domain.accountlist.entities.GeneralAccountDetails
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.register.RegisterV2Param
import com.tokopedia.sessioncommon.domain.usecase.GetRegisterV2AndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScpAuthViewModel @Inject constructor(
    val getUserInfoAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    val registerV2AndSaveSessionUseCase: GetRegisterV2AndSaveSessionUseCase,
    val userSessionInterface: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _onLoginSuccess = SingleLiveEvent<Boolean>()
    val onLoginSuccess: LiveData<Boolean> = _onLoginSuccess

    private val _showFullScreenLoading = MutableLiveData<Boolean>()
    val showFullScreenLoading: LiveData<Boolean> = _showFullScreenLoading

    fun getUserInfo() {
        launch {
            try {
                GotoSdk.LSDKINSTANCE?.getAccessToken()?.let {
                    updateToken()
                }
                getUserInfoAndSaveSessionUseCase(Unit)
                AuthAnalyticsMapper.trackProfileFetch("success")
                _onLoginSuccess.postValue(true)
            } catch (e: Exception) {
                AuthAnalyticsMapper.trackProfileFetch("failed - ${e.message}")
                _onLoginSuccess.postValue(false)
            }
        }
    }

    fun register(account: GeneralAccountDetails) {
        _showFullScreenLoading.postValue(true)
        launch {
            try {
                val params = RegisterV2Param(
                    regType = "progressive_sso",
                    email = account.email,
                    phone = account.phoneNumber,
                    fullName = account.fullname,
                    auth_code = account.token
                )
                when (val resp = registerV2AndSaveSessionUseCase(params)) {
                    is Success -> {
                        ScpUtils.saveTokens(resp.data.accessToken, resp.data.refreshToken)
                        _onLoginSuccess.postValue(true)
                    }
                    is Fail -> {
                        _onLoginSuccess.postValue(false)
                    }
                }
            } catch (e: Exception) {
                _onLoginSuccess.postValue(false)
            } finally {
                _showFullScreenLoading.postValue(false)
            }
        }
    }

    private fun updateToken() {
        userSessionInterface.setToken(
            GotoSdk.LSDKINSTANCE?.getAccessToken(),
            "Bearer",
            EncoderDecoder.Encrypt(GotoSdk.LSDKINSTANCE?.getRefreshToken(), userSessionInterface.refreshTokenIV)
        )
    }
}
