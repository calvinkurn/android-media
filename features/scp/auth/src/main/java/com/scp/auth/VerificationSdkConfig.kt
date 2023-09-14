package com.scp.auth

import com.scp.verification.core.domain.common.entities.config.VerificationAppConfig
import com.scp.verification.core.domain.common.entities.config.VerificationAuthConfig
import com.scp.verification.core.domain.common.entities.config.VerificationEnvironment
import com.scp.verification.core.domain.common.listener.VerificationSDKConfigs

class VerificationSdkConfig : VerificationSDKConfigs {
    override fun getAppConfigs(): VerificationAppConfig {
        return VerificationAppConfig(
            environment = VerificationEnvironment.DEV,
            isLogsEnabled = false,
            locale = "EN",
            userlang = "en",
            userType = "customer"
        )
    }

    override fun getAuthConfigs(): VerificationAuthConfig {
        return VerificationAuthConfig(clientID = "tokopedia:consumer:app", clientSecret = "qmcpRpZPBC7DTRNQiI7dIkuGoxrqsu")
    }
}
