package com.scp.auth

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.gojek.pin.AppInfo
import com.gojek.pin.DeviceInfo
import com.gojek.pin.PinConfig
import com.gojek.pin.PinManager
import com.gojek.pin.PinNetwork
import com.gojek.pin.PinProvider
import com.gojek.pin.validation.ExtVerificationData
import com.gojek.pin.validation.ExtVerificationUiConfig
import com.gojek.pin.validation.PinSdkValidationListener
import com.gojek.pin.validation.PinValidationResults
import com.scp.auth.verification.VerificationSdk.getCvSdkProvider
import com.scp.login.core.domain.contracts.configs.LSdkAppConfig
import com.scp.login.core.domain.contracts.configs.LSdkAuthConfig
import com.scp.login.core.domain.contracts.configs.LSdkConfig
import com.scp.login.core.domain.contracts.configs.LSdkEnvironment
import com.scp.login.core.domain.contracts.services.LSdkServices
import com.scp.login.init.GotoLogin
import com.scp.login.init.contracts.LSdkProvider
import com.scp.verification.core.data.common.services.LocalCVABTestService
import com.scp.verification.core.data.common.services.LocalCVLogService
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsEvent
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsService
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object GotoSdk {
    var LSDKINSTANCE: LSdkProvider? = null

    private var GOTOPINSDKINSTANCE: PinManager? = null

    private const val TOKOPEDIA_APP_TYPE = "Tokopedia"
    private const val DEVICE_TYPE = "android"
    private const val LOCALE_ID = "id"

    @JvmStatic
    fun init(application: Application): LSdkProvider? {
        LSDKINSTANCE = GotoLogin.getInstance(
            cvSdkProvider = getCvSdkProvider(application),
            gotoPinManager = getGotoPinSdkProvider(application),
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

    private fun getGotoPinSdkProvider(application: Application): PinManager {
        GOTOPINSDKINSTANCE = PinProvider.providePinManager(
            context = application,
            config = PinConfig(
                network = PinNetwork(
                    authBearerProvider = { "" },
                    okHttpClient = OkHttpClient().newBuilder().addInterceptor(
                        Interceptor { chain ->
                            val request = chain.request().newBuilder()
                            request.addHeader("X-User-Locale", "id-ID")
                            request.addHeader("Accept-Language", "id_ID")
                            chain.proceed(request.build())
                        }
                    ).build()
                ),
                appInfo = AppInfo(
                    appType = TOKOPEDIA_APP_TYPE,
                    isDebug = GlobalConfig.isAllowDebuggingTools(),
                    language = LOCALE_ID
                ),
                deviceInfo = DeviceInfo(
                    appVersion = GlobalConfig.VERSION_NAME,
                    deviceName = getDeviceName(),
                    osVersion = Build.VERSION.SDK_INT.toString(),
                    type = DEVICE_TYPE
                ),
                validationListener = object : PinSdkValidationListener {
                    override fun handleOtpError(errorCode: Int, errorMessage: String, coroutineScope: CoroutineScope) {
                    }

                    override fun provideAuthenticationResult(context: AppCompatActivity,
                        uiConfig: ExtVerificationUiConfig,
                        data: ExtVerificationData, callback: (PinValidationResults) -> Unit) {
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
}

class SampleLoginSDKConfigs(val context: Context) : LSdkConfig {
    override fun getAppConfigs(): LSdkAppConfig {
        val uniqueId = FingerprintModelGenerator.getFCMId(context)
        return LSdkAppConfig(
            environment = LSdkEnvironment.DEV,
            isLogsEnabled = false,
            appLocale = "ID",
            userLang = "id",
            userType = "toko_user",
            uniqueId = uniqueId
        )
    }

    override fun getAuthConfigs(): LSdkAuthConfig {
        return LSdkAuthConfig(clientID = "tokopedia:consumer:android", clientSecret = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g", gotoPinclientID = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g")
    }
}
