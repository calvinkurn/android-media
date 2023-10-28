package com.scp.auth.verification

import android.content.Context
import com.scp.auth.common.utils.ScpConstants
import com.scp.verification.core.domain.common.entities.config.VerificationAppConfig
import com.scp.verification.core.domain.common.entities.config.VerificationAuthConfig
import com.scp.verification.core.domain.common.entities.config.VerificationEnvironment
import com.scp.verification.core.domain.common.listener.VerificationSDKConfigs
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.keys.Keys
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

class VerificationSdkConfig(val context: Context) : VerificationSDKConfigs {
    override fun getAppConfigs(): VerificationAppConfig {
        return VerificationAppConfig(
            environment = getEnvironment(),
            isLogsEnabled = isLogEnabled(),
            locale = ScpConstants.ID_LANGUAGE,
            userlang = ScpConstants.ID_LANGUAGE,
            userType = ScpConstants.TOKO_USER_TYPE,
            uniqueId = FingerprintModelGenerator.getFCMId(context)
        )
    }

    private fun getClientSecret(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) ScpConstants.DEBUG_CLIENT_SECRET else Keys.LSDK_KEY_MA
    }

    override fun getAuthConfigs(): VerificationAuthConfig {
        return VerificationAuthConfig(
            clientID = ScpConstants.TOKOPEDIA_CLIENT_ID,
            clientSecret = getClientSecret()
        )
    }

    private fun getEnvironment(): VerificationEnvironment {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            VerificationEnvironment.INTEGRATION
        } else {
            VerificationEnvironment.PROD
        }
    }

    private fun isLogEnabled(): Boolean = GlobalConfig.isAllowDebuggingTools()
}
