package com.scp.auth.authentication

import android.content.Context
import com.scp.login.core.domain.contracts.configs.LSdkAppConfig
import com.scp.login.core.domain.contracts.configs.LSdkAuthConfig
import com.scp.login.core.domain.contracts.configs.LSdkConfig
import com.scp.login.core.domain.contracts.configs.LSdkEnvironment
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator

class LoginSdkConfigs(val context: Context) : LSdkConfig {
    override fun getAppConfigs(): LSdkAppConfig {
        val uniqueId = FingerprintModelGenerator.getFCMId(context)
        return LSdkAppConfig(
            environment = LSdkEnvironment.INTEGRATION,
            isLogsEnabled = false,
            appLocale = "id-ID",
            userLang = "id-ID",
            userType = "toko_user",
            uniqueId = uniqueId
        )
    }

    override fun getAuthConfigs(): LSdkAuthConfig {
        return LSdkAuthConfig(clientID = "tokopedia:consumer:android", clientSecret = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g", gotoPinclientID = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g")
    }
}
