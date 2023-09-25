package com.scp.auth.verification

import android.content.Context
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
            locale = ID_LANGUAGE,
            userlang = ID_LANGUAGE,
            userType = TOKO_USER_TYPE,
            uniqueId = FingerprintModelGenerator.getFCMId(context)
        )
    }

    override fun getAuthConfigs(): VerificationAuthConfig {
        return VerificationAuthConfig(
            clientID = context.getString(keysR.string.cvsdk_client_id),
            clientSecret = context.getString(keysR.string.cvsdk_client_secret)
        )
    }

    private fun getEnvironment(): VerificationEnvironment {
        return if (GlobalConfig.DEBUG) VerificationEnvironment.DEV
        else VerificationEnvironment.PROD
    }

    private fun isLogEnabled(): Boolean = GlobalConfig.isAllowDebuggingTools()

    companion object {
        private const val TOKO_USER_TYPE = "toko_user"
        private const val ID_LANGUAGE = "id"
    }
}
