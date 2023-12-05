package com.scp.auth.authentication

import android.content.Context
import com.scp.auth.common.utils.ScpConstants
import com.scp.auth.common.utils.ScpUtils
import com.scp.login.core.domain.contracts.configs.LSdkAppConfig
import com.scp.login.core.domain.contracts.configs.LSdkAuthConfig
import com.scp.login.core.domain.contracts.configs.LSdkConfig
import com.scp.login.core.domain.contracts.configs.LSdkEnvironment
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.keys.Keys
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

class LoginSdkConfigs(val context: Context) : LSdkConfig {
    override fun getAppConfigs(): LSdkAppConfig {
        val uniqueId = FingerprintModelGenerator.getFCMId(context)
        return LSdkAppConfig(
            environment = getEnvironment(),
            isLogsEnabled = false,
            appLocale = ScpConstants.APP_LOCALE,
            userLang = ScpConstants.APP_LOCALE,
            userType = ScpConstants.TOKO_USER_TYPE,
            uniqueId = uniqueId,
            isSupportProgressiveSignup = ScpUtils.isProgressiveSignupEnabled()
        )
    }

    private fun getClientSecret(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) ScpConstants.DEBUG_CLIENT_SECRET else Keys.LSDK_KEY_MA
    }

    private fun getGotoPinSecret(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) ScpConstants.GOTO_PIN_DEBUG_SECRET else Keys.GTP_KEY_MA
    }

    override fun getAuthConfigs(): LSdkAuthConfig {
        return LSdkAuthConfig(clientID = ScpConstants.TOKOPEDIA_CLIENT_ID, clientSecret = getClientSecret(), gotoPinclientID = getGotoPinSecret())
    }

    private fun getEnvironment(): LSdkEnvironment {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            LSdkEnvironment.INTEGRATION
        } else {
            LSdkEnvironment.PROD
        }
    }
}
