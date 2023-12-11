package com.scp.auth.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.scp.auth.GotoSdk
import com.scp.auth.common.analytics.AuthAnalyticsMapper
import com.scp.auth.common.utils.ScpConstants
import com.scp.auth.common.utils.ScpUtils
import com.scp.auth.common.utils.ScpUtils.createGenericBottomSheet
import com.scp.auth.common.utils.ScpUtils.createNoConnectionBottomSheet
import com.scp.auth.common.utils.TkpdAdditionalHeaders
import com.scp.auth.common.utils.goToForgotGotoPin
import com.scp.auth.common.utils.goToForgotPassword
import com.scp.auth.common.utils.goToForgotTokoPinArticle
import com.scp.auth.common.utils.goToHelpGotoPIN
import com.scp.auth.common.utils.goToInactivePhoneNumber
import com.scp.auth.di.DaggerScpAuthComponent
import com.scp.auth.registerpushnotif.services.ScpRegisterPushNotificationWorker
import com.scp.auth.service.GetDefaultChosenAddressService
import com.scp.login.common.utils.LoginImageLoader
import com.scp.login.core.domain.accountlist.entities.GeneralAccountDetails
import com.scp.login.core.domain.common.UserCredential
import com.scp.login.core.domain.contracts.configs.LSdkChooseAccountUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkInputCredentialsUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkSsoUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkUiConfig
import com.scp.login.core.domain.contracts.listener.LSdkClientFlowListener
import com.scp.login.core.domain.contracts.listener.LSdkLoginFlowListener
import com.scp.login.core.utils.DeviceUtils.hideKeyboard
import com.scp.verification.core.domain.common.entities.Failure
import com.scp.verification.core.domain.common.listener.ForgetContext
import com.scp.verification.features.gotopin.CVPinManager
import com.scp.verification.utils.gone
import com.scp.verification.utils.visible
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
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject


class ScpAuthActivity : BaseActivity() {

    @Inject
    lateinit var firebaseRemoteConfig: RemoteConfig

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var isHitRegisterPushNotif: Boolean = false

    private var noConnectionBottomSheet: BottomSheetUnify? = null
    private var genericErrorBottomSheet: BottomSheetUnify? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            ScpAuthViewModel::class.java
        )
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // lost network connection
        override fun onLost(network: Network) {
            showNoConnectionBottomSheet()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerNetworkCallback()

        GotoSdk.setActivty(this)
        val binding = ActivityScpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(
            "android_user_register_otp_push_notif_login_page",
            false
        )

        if (intent.hasExtra(ApplinkConstInternalUserPlatform.PARAM_FROM_WEBVIEW)) {
            handleRefreshTokenCase()
        } else {
            startGotoLogin()
        }

        with(viewModel) {
            onLoginSuccess.observe(this@ScpAuthActivity) {
                if (it) {
                    postLoginAction()
                } else {
                    handleError("Terjadi Kesalahan")
                }
            }
            onProgressiveSignupSuccess.observe(this@ScpAuthActivity) {
                if (it.isNotEmpty()) {
                    AuthAnalyticsMapper.trackProgressiveSignupSuccess()
                    postLoginAction()
                } else {
                    showGenericErrorBottomSheet()
                }
            }
            onRefreshSuccess.observe(this@ScpAuthActivity) {
                if (it) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    startGotoLogin()
                }
            }
            showFullScreenLoading.observe(this@ScpAuthActivity) {
                if (it) {
                    binding.screenLoader.root.visible()
                } else {
                    binding.screenLoader.root.gone()
                }
            }
        }
    }

    // Handle refresh token for webview
    private fun handleRefreshTokenCase() {
        if(userSession.accessToken.isNotEmpty() && userSession.freshToken.isNotEmpty()) {
            viewModel.triggerRefreshToken()
        } else {
            startGotoLogin()
        }
    }

    private fun handleError(errorMsg: String) {
        try {
            ScpUtils.clearTokens()
            Toast.makeText(this@ScpAuthActivity, errorMsg, Toast.LENGTH_LONG).show()
            finish()
        } catch (ignored: Exception) {}
    }

    fun initComponents() {
        val appComponent = (application as BaseMainApplication)
            .baseAppComponent
        DaggerScpAuthComponent.builder()
            .baseAppComponent(appComponent)
            .build()
            .inject(this)
    }

    private fun isShowGoogleLogin(): Boolean {
        return firebaseRemoteConfig.getBoolean(CONFIG_GOOGLE_LOGIN, false)
    }

    private fun registerNetworkCallback() {
        val context = applicationContext ?: return
        val connectivityManager = ContextCompat.getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            } else {
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            }
        } catch (ignored: Exception) { }
    }

    private fun startGotoLogin() {
        GotoSdk.LSDKINSTANCE?.startLoginFlow(
            activity = this@ScpAuthActivity,
            uiConfig = LSdkUiConfig(
                shouldOpenLoginScreen = true,
                isHelpCenterVisible = true,
                shouldManuallyUpdateLanguage = true,
                ssoUiConfigs = LSdkSsoUiConfigs(
                    title = "Selamat datang di Tokopedia!",
                    subtitle = "Masuk atau daftar hanya dalam beberapa langkah mudah"
                ),
                chooseAccountConfigs = LSdkChooseAccountUiConfigs(
                    title = "Pilih akun untuk lanjut"
                ),
                inputCredentialsConfigs = LSdkInputCredentialsUiConfigs(
                    shouldShowGoogleLogin = isShowGoogleLogin()
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

                override fun onProgressiveSignupFlow(accountDetails: GeneralAccountDetails) {
                    if (ScpUtils.isProgressiveSignupEnabled()) {
                        GotoSdk.LSDKINSTANCE?.closeScreenAndExit()
                        // track click signup tracker row 2
                        viewModel.register(accountDetails)
                    } else {
                        val credential = if (accountDetails.phoneNumber.isNotEmpty()) {
                            UserCredential(
                                phoneNumber = accountDetails.phoneNumber,
                                countryCode = accountDetails.countryCode
                            )
                        } else UserCredential(email = accountDetails.email)
                        gotoRegisterInitial(credential)
                    }
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

        initTokoChatConnection()

        saveFirstInstallTime()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun saveFirstInstallTime() {
        val sharedPrefs = getSharedPreferences(
            ScpConstants.KEY_FIRST_INSTALL_SEARCH,
            Context.MODE_PRIVATE
        )
        sharedPrefs?.edit()?.putLong(
            ScpConstants.KEY_FIRST_INSTALL_TIME_SEARCH,
            0
        )?.apply()
    }

    private fun initTokoChatConnection() {
        if (application is AbstractionRouter) {
            (application as AbstractionRouter).connectTokoChat(true)
        }
    }

    private fun getDefaultChosenAddress() {
        GetDefaultChosenAddressService.startService(applicationContext)
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
                loginEventAppsFlyer(userSession.userId, "")
            }

            AuthAnalyticsMapper.trackMoUser(this, userSession.loginMethod)
        } catch (ignored: Exception) { }
    }

    override fun onDestroy() {
        super.onDestroy()
        GotoSdk.setActivty(null)
    }

    private fun loginEventAppsFlyer(userId: String, userEmail: String) {
        val dataMap = HashMap<String, Any>()
        dataMap["user_id"] = userId
        dataMap["user_email"] = userEmail
        val date = Date()
        val stringDate = DateFormat.format("EEEE, MMMM d, yyyy ", date.time)
        dataMap["timestamp"] = stringDate
        TrackApp.getInstance().appsFlyer.sendTrackEvent("Login Successful", dataMap)
    }

    fun showNoConnectionBottomSheet() {
        hideKeyboard()
        noConnectionBottomSheet = if (noConnectionBottomSheet == null) createNoConnectionBottomSheet { finish() } else noConnectionBottomSheet
        if (noConnectionBottomSheet?.isAdded == false) {
            noConnectionBottomSheet?.show(supportFragmentManager,  "")
        }
    }

    fun showGenericErrorBottomSheet() {
        hideKeyboard()
        genericErrorBottomSheet = if (genericErrorBottomSheet == null) createGenericBottomSheet { finish() } else genericErrorBottomSheet
        if (genericErrorBottomSheet?.isAdded == false) {
            genericErrorBottomSheet?.show(supportFragmentManager, "")
        }
    }

    companion object {
        const val TOKOPEDIA_CARE_PATH = "help"
        const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"

        const val CONFIG_GOOGLE_LOGIN = "android_user_google_login"
    }
}
