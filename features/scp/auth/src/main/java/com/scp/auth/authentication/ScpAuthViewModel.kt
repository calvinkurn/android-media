package com.scp.auth.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scp.auth.GotoSdk
import com.scp.auth.common.analytics.AuthAnalyticsMapper
import com.scp.auth.common.utils.ScpUtils
import com.scp.auth.domain.StatusUseCase
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
    private val getUserInfoAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    private val registerV2AndSaveSessionUseCase: GetRegisterV2AndSaveSessionUseCase,
    private val statusUseCase: StatusUseCase,
    val userSessionInterface: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _onLoginSuccess = MutableLiveData<Boolean>()
    val onLoginSuccess: LiveData<Boolean> = _onLoginSuccess

    private val _onRefreshSuccess = SingleLiveEvent<Boolean>()
    val onRefreshSuccess: LiveData<Boolean> = _onRefreshSuccess

    private val _onProgressiveSignupSuccess = MutableLiveData<String>()
    val onProgressiveSignupSuccess: LiveData<String> = _onProgressiveSignupSuccess

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
                    gotoAccountId = account.accountId,
                    gotoAuthCode = account.token
                )
                when (val resp = registerV2AndSaveSessionUseCase(params)) {
                    is Success -> {
                        AuthAnalyticsMapper.trackProgressiveSignupSuccess()
                        ScpUtils.saveTokens(resp.data.accessToken, resp.data.refreshToken)
                        getUserInfoAndSaveSessionUseCase(Unit)
                        _onProgressiveSignupSuccess.postValue(account.fullname)
                    }
                    is Fail -> {
                        _onProgressiveSignupSuccess.postValue("")
                    }
                }
            } catch (e: Exception) {
                AuthAnalyticsMapper.trackProgressiveSignupFailed(e.message ?: "")
                _onProgressiveSignupSuccess.postValue("")
            } finally {
                _showFullScreenLoading.postValue(false)
            }
        }
    }

    // execute status query to trigger refresh token flow
    fun triggerRefreshToken() {
        launch {
            try {
                statusUseCase(Unit)
                _onRefreshSuccess.postValue(true)
            } catch (ignored: Exception) {
                _onRefreshSuccess.postValue(false)
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
