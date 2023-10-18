package com.scp.auth.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.scp.auth.GotoSdk
import com.scp.auth.common.analytics.AuthAnalyticsMapper
import com.scp.auth.common.utils.TkpdAdditionalHeaders
import com.scp.auth.common.utils.goToForgotGotoPin
import com.scp.auth.common.utils.goToForgotPassword
import com.scp.auth.common.utils.goToForgotTokoPinArticle
import com.scp.auth.common.utils.goToHelpGotoPIN
import com.scp.auth.common.utils.goToInactivePhoneNumber
import com.scp.auth.di.DaggerScpAuthComponent
import com.scp.auth.registerpushnotif.services.ScpRegisterPushNotificationWorker
import com.scp.login.common.utils.LoginImageLoader
import com.scp.login.core.domain.common.UserCredential
import com.scp.login.core.domain.contracts.configs.LSdkChooseAccountUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkSsoUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkUiConfig
import com.scp.login.core.domain.contracts.listener.LSdkClientFlowListener
import com.scp.login.core.domain.contracts.listener.LSdkLoginFlowListener
import com.scp.verification.core.domain.common.entities.Failure
import com.scp.verification.core.domain.common.listener.ForgetContext
import com.scp.verification.features.gotopin.CVPinManager
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.analytics.firebase.TkpdFirebaseAnalytics
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiConstant
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.scp.auth.databinding.ActivityScpAuthBinding
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
import com.tokopedia.track.TrackApp
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ScpAuthActivity : BaseActivity() {

    @Inject
    lateinit var firebaseRemoteConfig: RemoteConfig

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var isHitRegisterPushNotif: Boolean = false

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            ScpAuthViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityScpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(
            "android_user_register_otp_push_notif_login_page",
            false
        )

        startGotoLogin()

        with(viewModel) {
            onLoginSuccess.observe(this@ScpAuthActivity) {
                postLoginAction()
            }
        }
    }

    fun initComponents() {
        val appComponent = (application as BaseMainApplication)
            .baseAppComponent
        DaggerScpAuthComponent.builder()
            .baseAppComponent(appComponent)
            .build()
            .inject(this)
    }

    private fun startGotoLogin() {
        GotoSdk.LSDKINSTANCE?.startLoginFlow(
            activity = this@ScpAuthActivity,
            uiConfig = LSdkUiConfig(
                isHelpCenterVisible = true,
                shouldManuallyUpdateLanguage = true,
                ssoUiConfigs = LSdkSsoUiConfigs(
                    title = "Selamat datang di Tokopedia!",
                    subtitle = "Masuk atau daftar hanya dalam beberapa langkah mudah"
                ),
                chooseAccountConfigs = LSdkChooseAccountUiConfigs(
                    title = "Pilih akun untuk lanjut"
                )
            ),
            additionalHeaders = TkpdAdditionalHeaders(this),
            loginSuccessListener = object : LSdkLoginFlowListener {
                override fun onUserBackPressed(closeLogin: () -> Unit) {
                    GotoSdk.LSDKINSTANCE?.closeScreenAndExit()
                    finish()
                }

                override fun onLoginError(failure: Failure, activity: Activity?) {
                }

                override fun onLoginSuccessful(activity: Activity?) {
                    GotoSdk.LSDKINSTANCE?.closeScreenAndExit()
                    getUserInfo()
                }

                override fun onUserNotRegistered(credential: UserCredential, activity: Activity?) {
                    gotoRegisterInitial(credential)
                }
            },
            clientFlowListener = object : LSdkClientFlowListener {
                override fun onHelpCentreClicked(
                    screenType: String,
                    isGotoPinHelpContext: Boolean
                ) {
                    if (isGotoPinHelpContext) {
                        when (screenType) {
                            CVPinManager.CTA_TYPE_HELP -> {
                                goToHelpGotoPIN(this@ScpAuthActivity)
                            }

                            CVPinManager.FORGOT_PIN_HELP_ARTICLE -> {
                                goToForgotGotoPin(this@ScpAuthActivity)
                            }

                            else -> {
                                /* no-op */
                            }
                        }
                    } else {
                        goToTokopediaCare()
                    }
                }

                override fun onAccountRecoverClicked() {
                    goToInactivePhoneNumber(this@ScpAuthActivity)
                }

                override fun onForgetClicked(forgetType: String) {
                    goToForgetScreen(forgetType)
                }

                override fun onTermsServicesClicked() {
                    startActivity(
                        RouteManager.getIntent(
                            this@ScpAuthActivity,
                            ApplinkConstInternalUserPlatform.TERM_PRIVACY,
                            PAGE_TERM_AND_CONDITION
                        )
                    )
                }

                override fun onPrivacyPolicyClicked() {
                    startActivity(
                        RouteManager.getIntent(
                            this@ScpAuthActivity,
                            ApplinkConstInternalUserPlatform.TERM_PRIVACY,
                            PAGE_PRIVACY_POLICY
                        )
                    )
                }

                override fun onLanguageSelectorClicked(activity: Activity, source: String) {}
            },
            imageLoader = object : LoginImageLoader {
                override fun loadBitmapFromUrl(
                    context: Context,
                    url: String,
                    onSuccess: (Bitmap) -> Unit,
                    onFailure: () -> Unit
                ) {
                }
            }
        )
    }

    private fun setFCM() {
        (application as AbstractionRouter).onRefreshCM(userSession.deviceId)
    }

    private fun gotoRegisterInitial(userCredential: UserCredential) {
        val intent = RouteManager.getIntent(this@ScpAuthActivity, ApplinkConstInternalUserPlatform.INIT_REGISTER).apply {
            val userCredential = userCredential.phoneNumber.ifEmpty { userCredential.email }
            putExtra(ApplinkConstInternalUserPlatform.LOGIN_SDK_CREDENTIAL, userCredential)
            flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        }
        startActivity(intent)
        finish()
    }

    private fun getUserInfo() {
        viewModel.getUserInfo()
        AuthAnalyticsMapper.trackProfileFetch("triggered")
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            this,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun goToForgetScreen(type: String) {
        when (type) {
            ForgetContext.FORGET_PASSWORD.name -> {
                goToForgotPassword(this)
            }

            ForgetContext.FORGET_TOKO_PIN.name -> {
                goToForgotTokoPinArticle(this)
            }
        }
    }

    private fun submitIntegrityApi() {
        if (firebaseRemoteConfig.getBoolean(IntegrityApiConstant.LOGIN_CONFIG)) {
            IntegrityApiWorker.scheduleWorker(applicationContext, IntegrityApiConstant.EVENT_LOGIN)
        }
    }

    private fun registerPushNotif() {
        if (isHitRegisterPushNotif && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ScpRegisterPushNotificationWorker.scheduleWorker(this)
        }
    }

    fun postLoginAction() {
        registerPushNotif()
        submitIntegrityApi()
        setTrackingUserId(userSession.userId)
        setFCM()
        SubmitDeviceWorker.scheduleWorker(this, true)
        DataVisorWorker.scheduleWorker(this, true)
        TwoFactorMluHelper.clear2FaInterval(this)
        getDefaultChosenAddress()
        RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getDefaultChosenAddress() {
//        GetDefaultChosenAddressService.startService(applicationContext)
    }

    private fun setTrackingUserId(userId: String) {
        try {
            val ctx = this.applicationContext
            TkpdAppsFlyerMapper.getInstance(ctx).mapAnalytics()
            TrackApp.getInstance().gtm.pushUserId(userId)
            val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
            if (!GlobalConfig.DEBUG) {
                crashlytics.setUserId(userId)
            }
            ctx?.let {
                TkpdFirebaseAnalytics.getInstance(ctx).setUserId(userId)
            }

            if (userSession.isLoggedIn) {
                val userData = com.tokopedia.linker.model.UserData()
                userData.userId = userSession.userId
                userData.medium = userSession.loginMethod

                // Identity Event
                LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY, userData)
                )

                // Login Event
                LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGIN_VAL, userData)
                )
//                loginEventAppsFlyer(userSession.userId, "")
            }

//            TrackApp.getInstance().moEngage.setMoEUserAttributesLogin(
//                userSession.userId,
//                "",
//                "",
//                "",
//                userSession.isGoldMerchant,
//                userSession.shopName,
//                userSession.shopId,
//                userSession.hasShop(),
//                analytics.getLoginMethodMoengage(userSession.loginMethod)
//            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TOKOPEDIA_CARE_PATH = "help"
        const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
    }
}
