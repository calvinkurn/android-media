package com.scp.auth

import android.app.Application
import android.content.Context
import android.os.Build
import com.gojek.pin.AppInfo
import com.gojek.pin.DeviceInfo
import com.gojek.pin.PinConfig
import com.gojek.pin.PinManager
import com.gojek.pin.PinNetwork
import com.gojek.pin.PinProvider
import com.gojek.pin.validation.PinSdkValidationListener
import com.gojek.pin.validation.PinValidationResults
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
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object GotoSdk {
    var LSDKINSTANCE: LSdkProvider? = null
    var CVSDKINSTANCE: CvSdkProvider? = null
    var GOTOPINSDKINSTANCE: PinManager? = null

    private const val TOKOPEDIA_APP_TYPE = "Tokopedia"

    @JvmStatic
    fun init(application: Application): LSdkProvider? {
        LSDKINSTANCE = GotoLogin.getInstance(
            cvSdkProvider = getCvSdkProvider(application),
            gotoPinManager = getPinManager(application),
            configurations = SampleLoginSDKConfigs(application),
            application = application,
            services = LSdkServices(
                abTestServices = LocalCVABTestService(),
                logService = LocalCVLogService(),
                analyticsService = object : ScpAnalyticsService {
                    override fun trackView(
                        eventName: ScpAnalyticsEvent,
                        params: Map<String, Any?>
                    ) {
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
                    }
                }
            )
        )
        return LSDKINSTANCE
    }

    private fun getCvSdkProvider(application: Application): CvSdkProvider {
        CVSDKINSTANCE = GotoVerification.getInstance(
            context = application,
            configurations = VerificationSdkConfig(application),
            services = VerificationServices(),
            identifier = CvsdkFlowType.Main
        )
        return CVSDKINSTANCE!!
    }

    private fun getGotoPinSdkProvider(application: Application): PinManager {
        GOTOPINSDKINSTANCE = PinProvider.providePinManager(
            context = application,
            config = PinConfig(
                network = PinNetwork(
                    okHttpClient = OkHttpClient().newBuilder().build()
                ),
                appInfo = AppInfo(
                    appType = TOKOPEDIA_APP_TYPE,
                    isDebug = GlobalConfig.isAllowDebuggingTools(),
                    language = "id"
                ),
                deviceInfo = DeviceInfo(
                    appVersion = GlobalConfig.VERSION_NAME,
                    deviceName = getDeviceName(),
                    osVersion = Build.VERSION.SDK_INT.toString(),
                    type = "android"
                ),
                validationListener = object : PinSdkValidationListener {
                    override fun handleOtpError(errorCode: Int, errorMessage: String, coroutineScope: CoroutineScope) {
                    }

                    override fun provideAuthenticationResult(context: Context, callback: (PinValidationResults) -> Unit) {
                    }
                }
            )
        )
        return GOTOPINSDKINSTANCE!!
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        return "$manufacturer $model"
    }

    private fun getPinManager(application: Application): PinManager {
        return PinProvider.providePinManager(
            context = application,
            config = PinConfig(
                network = PinNetwork(
                    okHttpClient = OkHttpClient().newBuilder()
                        .addInterceptor(HttpLoggingInterceptor())
                        .addInterceptor(Interceptor { chain ->
                            val request = chain.request().newBuilder()
                            request.addHeader("X-User-Locale", "EN_en")
                            request.addHeader("Accept-Language", "EN_en")
                            chain.proceed(request.build())
                        })
                        .build()
                ),
                appInfo = AppInfo(
                    appType = "GoJek",
                    isDebug = true,
                    language = "en"
                ),
                deviceInfo = DeviceInfo(
                    appVersion = "",
                    deviceName = "",
                    osVersion = "",
                    type = ""
                ),
                validationListener = object : PinSdkValidationListener {
                    override fun handleOtpError(
                        errorCode: Int,
                        errorMessage: String,
                        coroutineScope: CoroutineScope
                    ) {
                        Log.d("lsdk", "gotopin otp error $errorCode $errorMessage")
                    }

                    override fun provideAuthenticationResult(
                        context: Context,
                        callback: (PinValidationResults) -> Unit
                    ) {
                        Log.d("lsdk", "gotopin authentication result")
                    }
                },
                analyticDelegate = {

                }
            )
        )
    }

}

class SampleLoginSDKConfigs(val context: Context) : LSdkConfig {
    override fun getAppConfigs(): LSdkAppConfig {
        return LSdkAppConfig(
            environment = LSdkEnvironment.DEV,
            isLogsEnabled = false,
            appLocale = "ID",
            userLang = "id",
            userType = "toko_user",
            uniqueId = FingerprintModelGenerator.getFCMId(context)
        )
    }

    override fun getAuthConfigs(): LSdkAuthConfig {
        return LSdkAuthConfig(clientID = "tokopedia:consumer:android", clientSecret = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g", gotoPinclientID = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g")
    }


}
