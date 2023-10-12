package com.scp.auth.common.utils

import com.scp.auth.GotoSdk
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance

object ScpUtils {
    fun isGotoLoginEnabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString("exp_scp_goto_login_sdk")
            .isNotEmpty() && GlobalConfig.isSellerApp().not()
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
}
