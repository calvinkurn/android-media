package com.scp.auth.authentication

import androidx.lifecycle.LiveData
import com.scp.auth.GotoSdk
import com.scp.auth.common.analytics.AuthAnalyticsMapper
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScpAuthViewModel @Inject constructor(
    val getUserInfoAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    val userSessionInterface: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _onLoginSuccess = SingleLiveEvent<Boolean>()
    val onLoginSuccess: LiveData<Boolean> = _onLoginSuccess

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

    private fun updateToken() {
        userSessionInterface.setToken(
            GotoSdk.LSDKINSTANCE?.getAccessToken(),
            "Bearer",
            EncoderDecoder.Encrypt(GotoSdk.LSDKINSTANCE?.getRefreshToken(), userSessionInterface.refreshTokenIV)
        )
    }
}
