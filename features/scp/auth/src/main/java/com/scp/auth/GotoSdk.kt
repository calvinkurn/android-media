package com.scp.auth

import android.app.Application
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
import com.scp.auth.authentication.LoginSdkConfigs
import com.scp.auth.common.analytics.AuthAnalyticsMapper
import com.scp.auth.common.analytics.GotoPinAnalyticsMapper
import com.scp.auth.common.utils.ScpUtils
import com.scp.auth.di.DaggerScpAuthComponent
import com.scp.auth.di.ScpAuthComponent
import com.scp.auth.verification.VerificationSdk
import com.scp.login.core.domain.common.infrastructure.LSdkEventName
import com.scp.login.core.domain.contracts.services.LSdkServices
import com.scp.login.init.GotoLogin
import com.scp.login.init.contracts.LSdkProvider
import com.scp.verification.core.data.common.services.LocalCVABTestService
import com.scp.verification.core.data.common.services.LocalCVLogService
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsEvent
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsService
import com.scp.verification.core.domain.common.infrastructure.CVEventFieldName
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.config.GlobalConfig
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.util.EncoderDecoder
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object GotoSdk {
    @JvmField var LSDKINSTANCE: LSdkProvider? = null

    private var GOTOPINSDKINSTANCE: PinManager? = null
    private var component: ScpAuthComponent? = null
    private var scpAuthActivity: AppCompatActivity? = null

    private const val TOKOPEDIA_APP_TYPE = "Tokopedia"
    private const val DEVICE_TYPE = "android"
    private const val LOCALE_ID = "id"
    private const val X_USER_LOCALE_KEY = "X-User-Locale"
    private const val ACCEPT_LANGUAGE_KEY = "Accept-Language"
    private const val X_USER_LOCALE_VALUE = "id-ID"
    private const val ACCEPT_LANGUAGE_VALUE = "id_ID"

    internal fun getVerifComponent(): ScpAuthComponent? = component

    fun setActivty(activity: AppCompatActivity?) {
        scpAuthActivity = activity
    }

    fun getActivity(): AppCompatActivity? = scpAuthActivity

    fun getGotopinInstance(): PinManager? = GOTOPINSDKINSTANCE

    @JvmStatic
    fun init(application: Application): LSdkProvider? {
        val appComponent = (application as BaseMainApplication)
            .baseAppComponent
        component = DaggerScpAuthComponent.builder()
            .baseAppComponent(appComponent)
            .build()
        LSDKINSTANCE = GotoLogin.getInstance(
            cvSdkProvider = VerificationSdk.getInstance(application),
            gotoPinManager = getGotoPinSdkProvider(application),
            configurations = LoginSdkConfigs(application),
            application = application,
            services = LSdkServices(
                abTestServices = LocalCVABTestService(),
                logService = LocalCVLogService(),
                analyticsService = object : ScpAnalyticsService {
                    override fun trackView(
                        eventName: ScpAnalyticsEvent,
                        params: Map<String, Any?>
                    ) {
                        trackLsdk(eventName.eventName, params)
                    }

                    override fun trackError(
                        eventName: ScpAnalyticsEvent,
                        params: Map<String, Any?>
                    ) {
                        if (eventName.eventName == LSdkEventName.LSDK_VERIFICATION_FAIL || eventName.eventName == LSdkEventName.LSDK_LOGIN_FAIL) {
                            ScpUtils.logError(eventName.eventName, params)
                        }
                        trackLsdk(eventName.eventName, params)
                    }

                    override fun trackEvent(
                        eventName: ScpAnalyticsEvent,
                        params: MutableMap<String, Any?>
                    ) {
                        if (eventName.eventName == LSdkEventName.LSDK_LOGIN_SUCCESS) {
                            ScpUtils.updateUserSessionLoginMethod(application, params[CVEventFieldName.METHOD].toString())
                        }
                        trackLsdk(eventName.eventName, params)
                    }
                }
            )
        )
        migrateExistingToken(userSession = UserSession(application))
        return LSDKINSTANCE
    }

    /*
        Save existing user access token and refresh token (from old login flow) to sdk,
        so users doesn't have to re-login to get Login SDK functionalities.
    */
    @JvmStatic
    fun migrateExistingToken(userSession: UserSessionInterface) {
        if (userSession.isLoggedIn &&
            LSDKINSTANCE?.getAccessToken()?.isEmpty() == true &&
            LSDKINSTANCE?.getRefreshToken()?.isEmpty() == true
        ) {
            ScpUtils.saveTokens(accessToken = userSession.accessToken, refreshToken = EncoderDecoder.Decrypt(userSession.freshToken, userSession.refreshTokenIV))
        }
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
                            request.addHeader(X_USER_LOCALE_KEY, X_USER_LOCALE_VALUE)
                            request.addHeader(ACCEPT_LANGUAGE_KEY, ACCEPT_LANGUAGE_VALUE)
                            chain.proceed(request.build())
                        }
                    ).build()
                ),
                appInfo = AppInfo(
                    appType = TOKOPEDIA_APP_TYPE,
                    isDebug = TokopediaUrl.getInstance().TYPE == Env.STAGING,
                    language = { LOCALE_ID },
                    isDarkThemeEnabled = {
                        getActivity()?.isDarkMode() ?: false
                    }
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

                    override fun provideAuthenticationResult(
                        context: AppCompatActivity,
                        uiConfig: ExtVerificationUiConfig,
                        data: ExtVerificationData,
                        callback: (PinValidationResults) -> Unit
                    ) {
                    }
                },
                analyticDelegate = {
                    GotoPinAnalyticsMapper.trackEvent(it.eventName, it.properties)
                }
            )
        )
        return GOTOPINSDKINSTANCE!!
    }

    private fun trackLsdk(eventName: String, params: Map<String, Any?>) {
        AuthAnalyticsMapper.trackScreenLsdk(eventName, params)
        AuthAnalyticsMapper.trackEventLsdk(eventName, params)
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        return "$manufacturer $model"
    }
}
