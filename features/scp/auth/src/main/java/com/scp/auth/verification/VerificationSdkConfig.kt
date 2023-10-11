package com.scp.auth.verification

import android.content.Context
import com.scp.auth.ScpConstants
import com.scp.verification.core.domain.common.entities.config.VerificationAppConfig
import com.scp.verification.core.domain.common.entities.config.VerificationAuthConfig
import com.scp.verification.core.domain.common.entities.config.VerificationEnvironment
import com.scp.verification.core.domain.common.listener.VerificationSDKConfigs
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.keys.R as keysR

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

    override fun getAuthConfigs(): VerificationAuthConfig {
        return VerificationAuthConfig(
            clientID = ScpConstants.TOKOPEDIA_CLIENT_ID,
            clientSecret = context.getString(keysR.string.cvsdk_client_secret)
        )
    }

    private fun getEnvironment(): VerificationEnvironment {
//        return if (GlobalConfig.DEBUG) {
//            VerificationEnvironment.INTEGRATION
//        } else {
//            VerificationEnvironment.PROD
//        }
        // Hardcode for production testing, rama is working on this issue
        return VerificationEnvironment.ALPHA
    }

    private fun isLogEnabled(): Boolean = GlobalConfig.isAllowDebuggingTools()

}
