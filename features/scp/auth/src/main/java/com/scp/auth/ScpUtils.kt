package com.scp.auth

import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance

object ScpUtils {
    fun isGotoLoginEnabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString("exp_scp_goto_login_sdk")
            .isNotEmpty() && GlobalConfig.isSellerApp().not()
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        if (isGotoLoginEnabled()) {
            GotoSdk.LSDKINSTANCE?.save(accessToken, refreshToken)
        }
    }
}
