package com.scp.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.scp.login.sso.bridge.SSOHostBridge
import com.scp.login.sso.data.SSOHostData
import com.scp.login.sso.utils.Environment
import com.scp.verification.core.data.common.device.DeviceInfoImpl
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
): BaseViewModel(dispatcher.main) {
    private val _onLoginSuccess = SingleLiveEvent<Unit>()
    val onLoginSuccess: LiveData<Unit> = _onLoginSuccess

    fun getUserInfo() {
        launch {
            try {
                GotoSdk.LSDKINSTANCE?.getAccessToken()?.let {
                    updateToken()
                }
                getUserInfoAndSaveSessionUseCase(Unit)
                _onLoginSuccess.postValue(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
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

    fun updateSsoHostData(context: Context, token: String) {
        viewModelScope.launch {
            val ssoHostData = SSOHostData(
                clientId = "tokopedia:consumer:android",
                clientSecret = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g",
                deviceId = DeviceInfoImpl(context).getDeviceID(),
                environment = Environment.Integration
            )
            val ssoBridge = SSOHostBridge.getSsoHostBridge()
            ssoBridge.initBridge(context, ssoHostData)
            ssoBridge.saveAccessToken(context, token)
        }
    }

}
