package com.scp.auth.authentication

import android.content.Context
import com.scp.auth.ScpConstants
import com.scp.login.core.domain.contracts.configs.LSdkAppConfig
import com.scp.login.core.domain.contracts.configs.LSdkAuthConfig
import com.scp.login.core.domain.contracts.configs.LSdkConfig
import com.scp.login.core.domain.contracts.configs.LSdkEnvironment
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.keys.R as keysR

class LoginSdkConfigs(val context: Context) : LSdkConfig {
    override fun getAppConfigs(): LSdkAppConfig {
        val uniqueId = FingerprintModelGenerator.getFCMId(context)
        return LSdkAppConfig(
            environment = getEnvironment(),
            isLogsEnabled = false,
            appLocale = ScpConstants.APP_LOCALE,
            userLang = ScpConstants.APP_LOCALE,
            userType = ScpConstants.TOKO_USER_TYPE,
            uniqueId = uniqueId
        )
    }

    override fun getAuthConfigs(): LSdkAuthConfig {
        return LSdkAuthConfig(clientID = ScpConstants.TOKOPEDIA_CLIENT_ID, clientSecret = context.getString(keysR.string.lsdk_client_secret), gotoPinclientID = context.getString(keysR.string.goto_pin_client_id))
    }

    private fun getEnvironment(): LSdkEnvironment {
//        return if (GlobalConfig.DEBUG || GlobalConfig.isAllowDebuggingTools()) {
//            LSdkEnvironment.INTEGRATION
//        } else {
//            LSdkEnvironment.PROD
//        }
        // Hardcode for production testing, rama is working on this issue
        return LSdkEnvironment.ALPHA
    }
}
