package com.scp.auth

import android.app.Application
import android.content.Context
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
import com.scp.verification.core.data.common.services.LocalCVLogService
import com.scp.verification.core.data.common.services.VerificationServices
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsEvent
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsService
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator

object GotoSdk {
    var LSDKINSTANCE: LSdkProvider? = null
    var CVSDKINSTANCE: CvSdkProvider? = null

    @JvmStatic
    fun init(application: Application): LSdkProvider? {
        LSDKINSTANCE = GotoLogin.getInstance(
            cvSdkProvider = getCvSdkProvider(application),
            application = application,
            configurations = SampleLoginSDKConfigs(application),
            services = LSdkServices(
                abTestServices = LocalCVABTestService(),
                logService = LocalCVLogService(),
                analyticsService = object : ScpAnalyticsService {
                    override fun trackView(
                        eventName: ScpAnalyticsEvent,
                        params: Map<String, Any?>
                    ) {
                        println("sdkTrack:view: $eventName, $params")
                    }

                    override fun trackError(
                        eventName: ScpAnalyticsEvent,
                        params: Map<String, Any?>
                    ) {
                        println("sdkTrack:error: $eventName, $params")
                    }

                    override fun trackEvent(
                        eventName: ScpAnalyticsEvent,
                        params: MutableMap<String, Any?>
                    ) {
                        println("sdkTrack:event: $eventName, $params")
                    }
                }
            )
        )
        return LSDKINSTANCE
    }

    private fun getCvSdkProvider(application: Application): CvSdkProvider {
        return GotoVerification.getInstance(
            context = application,
            configurations = VerificationSdkConfig(application),
            services = VerificationServices(),
            identifier = CvsdkFlowType.Main
        )
    }
}

class SampleLoginSDKConfigs(val context: Context) : LSdkConfig {
    override fun getAppConfigs(): LSdkAppConfig {
        return LSdkAppConfig(
            environment = LSdkEnvironment.INTEGRATION,
            isLogsEnabled = false,
            appLocale = "ID",
            userLang = "id",
            userType = "toko_user",
            uniqueId = FingerprintModelGenerator.getFCMId(context)
        )
    }

    override fun getAuthConfigs(): LSdkAuthConfig {
        return LSdkAuthConfig(clientID = "tokopedia:consumer:android", clientSecret = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g")
    }


    companion object {
    }
}

