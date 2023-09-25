package com.scp.auth

import android.content.Context
import com.scp.verification.core.domain.common.entities.config.VerificationAppConfig
import com.scp.verification.core.domain.common.entities.config.VerificationAuthConfig
import com.scp.verification.core.domain.common.entities.config.VerificationEnvironment
import com.scp.verification.core.domain.common.listener.VerificationSDKConfigs
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator

class VerificationSdkConfig(val context: Context) : VerificationSDKConfigs {
    override fun getAppConfigs(): VerificationAppConfig {
        return VerificationAppConfig(
            environment = VerificationEnvironment.INTEGRATION,
            isLogsEnabled = false,
            locale = "EN",
            userlang = "en",
            userType = "toko_user",
            uniqueId = FingerprintModelGenerator.getFCMId(context)
        )
    }

    override fun getAuthConfigs(): VerificationAuthConfig {
        return VerificationAuthConfig(clientID = "tokopedia:consumer:android", clientSecret = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g")
    }
}
