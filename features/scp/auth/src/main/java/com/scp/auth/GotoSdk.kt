package com.scp.auth

import android.app.Application
import com.scp.login.core.domain.contracts.configs.LSdkAppConfig
import com.scp.login.core.domain.contracts.configs.LSdkAuthConfig
import com.scp.login.core.domain.contracts.configs.LSdkConfig
import com.scp.login.core.domain.contracts.configs.LSdkEnvironment
import com.scp.login.core.domain.contracts.services.LSdkServices
import com.scp.login.init.GotoLogin
import com.scp.login.init.contracts.LSdkProvider
import com.scp.verification.CvSdkProvider
import com.scp.verification.GotoVerification
import com.scp.verification.core.CvsdkFlowType
import com.scp.verification.core.data.common.services.LocalCVABTestService
import com.scp.verification.core.data.common.services.LocalCVAnalyticsService
import com.scp.verification.core.data.common.services.LocalCVLogService
import com.scp.verification.core.data.common.services.VerificationServices

object GotoSdk {
    var LSDKINSTANCE: LSdkProvider? = null
    var CVSDKINSTANCE: CvSdkProvider? = null

    @JvmStatic
    fun init(application: Application): LSdkProvider? {
        LSDKINSTANCE = GotoLogin.getInstance(
            cvSdkProvider = getCvSdkProvider(application),
            application = application,
            configurations = SampleLoginSDKConfigs(),
            services = LSdkServices(
                abTestServices = LocalCVABTestService(),
                logService = LocalCVLogService(),
                analyticsService = LocalCVAnalyticsService()
            )
        )
        return LSDKINSTANCE
    }

    private fun getCvSdkProvider(application: Application): CvSdkProvider {
        return GotoVerification.getInstance(
            context = application,
            configurations = VerificationSdkConfig(),
            services = VerificationServices(),
            identifier = CvsdkFlowType.Main
        )
    }
}

class SampleLoginSDKConfigs : LSdkConfig {
    override fun getAppConfigs(): LSdkAppConfig {
        return LSdkAppConfig(
            environment = LSdkEnvironment.INTEGRATION,
            isLogsEnabled = false,
            appLocale = "ID",
            userLang = "id",
            userType = "toko_user"
        )
    }

    override fun getAuthConfigs(): LSdkAuthConfig {
        return LSdkAuthConfig(clientID = "tokopedia:consumer:app", clientSecret = "qmcpRpZPBC7DTRNQiI7dIkuGoxrqsu")
    }


    companion object {
    }
}

