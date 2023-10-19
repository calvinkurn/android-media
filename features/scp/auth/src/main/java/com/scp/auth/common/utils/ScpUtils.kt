package com.scp.auth.common.utils

import android.content.Context
import com.scp.auth.GotoSdk
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession

object ScpUtils {

    private val ROLLENCE_KEY_SCP_LOGIN = "exp_scp_goto_login_sdk"
    fun isGotoLoginEnabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_KEY_SCP_LOGIN)
            .isNotEmpty() && GlobalConfig.isSellerApp().not()
    }

    fun clearTokens() {
        GotoSdk.LSDKINSTANCE?.updateSsoToken("")
        GotoSdk.LSDKINSTANCE?.save("", "")
    }

    fun updateSsoToken(accessToken: String) {
        GotoSdk.LSDKINSTANCE?.updateSsoToken(accessToken = accessToken)
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        if (isGotoLoginEnabled()) {
            updateSsoToken(accessToken)
            GotoSdk.LSDKINSTANCE?.save(accessToken, refreshToken)
        }
    }

    fun logError(errorType: String, param: Map<String, Any?>) {
        val message = mutableMapOf(
            ScpConstants.ERROR_TYPE to errorType
        )
        for ((key, value) in param) {
            message.put(key, value.toString())
        }
        ServerLogger.log(Priority.P1, ScpConstants.LOGGER_SCP_AUTH_TAG, message)
    }

    fun updateUserSessionLoginMethod(context: Context, loginMethod: String) {
        val userSession = UserSession(context)
        userSession.loginMethod = loginMethod
    }
}
