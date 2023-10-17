package com.scp.auth.common.utils

import com.scp.auth.GotoSdk
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfigInstance

object ScpUtils {

    private val ROLLENCE_KEY_SCP_LOGIN = "exp_scp_goto_login_sdk"
    fun isGotoLoginEnabled(): Boolean {
        println("isGotoLoginEnabled: cek is debug ${GlobalConfig.DEBUG} or allowing debug tools ${GlobalConfig.isAllowDebuggingTools()}")
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_KEY_SCP_LOGIN)
            .isNotEmpty() && GlobalConfig.isSellerApp().not()
    }

    fun updateSsoToken(accessToken: String) {
        println("updateSsoToken: cek is debug ${GlobalConfig.DEBUG} or allowing debug tools ${GlobalConfig.isAllowDebuggingTools()}")
        GotoSdk.LSDKINSTANCE?.updateSsoToken(accessToken = accessToken)
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        println("\"saveTokens: cek is debug ${GlobalConfig.DEBUG} or allowing debug tools ${GlobalConfig.isAllowDebuggingTools()}\"")
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
}
