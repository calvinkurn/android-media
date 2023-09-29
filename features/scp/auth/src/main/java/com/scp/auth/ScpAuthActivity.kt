package com.scp.auth

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.scp.auth.di.DaggerScpAuthComponent
import com.scp.auth.utils.goToChangePIN
import com.scp.auth.utils.goToForgotPassword
import com.scp.auth.utils.goToInactivePhoneNumber
import com.scp.login.common.utils.LoginImageLoader
import com.scp.login.core.domain.common.UserCredential
import com.scp.login.core.domain.contracts.configs.LSdkChooseAccountUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkSsoUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkUiConfig
import com.scp.login.core.domain.contracts.listener.LSdkClientFlowListener
import com.scp.login.core.domain.contracts.listener.LSdkLoginFlowListener
import com.scp.verification.core.domain.common.entities.Failure
import com.scp.verification.core.domain.common.listener.ForgetContext
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiConstant
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
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
                    title = "Select Account to continue"
                )
            ),
            additionalHeaders = TkpdAdditionalHeaders(this),
            loginSuccessListener = object : LSdkLoginFlowListener {
                override fun onUserBackPressed(closeLogin: () -> Unit) {
                    finish()
                }

                override fun onLoginError(failure: Failure, activity: Activity?) {
                }

                override fun onLoginSuccessful(activity: Activity?) {
                    GotoSdk.LSDKINSTANCE?.closeScreenAndExit()
                    viewModel.getUserInfo()
                }

                override fun onUserNotRegistered(credential: UserCredential, activity: Activity?) {
                    val intent = RouteManager.getIntent(this@ScpAuthActivity, ApplinkConstInternalUserPlatform.INIT_REGISTER).apply {
                        val userCredential = credential.phoneNumber.ifEmpty { credential.email }
                        putExtra(ApplinkConstInternalUserPlatform.LOGIN_SDK_CREDENTIAL, userCredential)
                    }
                    startActivity(intent)
                }
            },
            clientFlowListener = object : LSdkClientFlowListener {
                override fun onHelpCentreClicked(
                    screenType: String,
                    isGotoPinHelpContext: Boolean
                ) {
                    goToTokopediaCare()
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

                override fun onLanguageSelectorClicked(activity: Activity) {
                }
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
                goToChangePIN(this)
            }
        }
    }

    private fun submitIntegrityApi() {
        if (firebaseRemoteConfig.getBoolean(IntegrityApiConstant.LOGIN_CONFIG)) {
            IntegrityApiWorker.scheduleWorker(applicationContext, IntegrityApiConstant.EVENT_LOGIN)
        }
    }

    private fun registerPushNotif() {
//        if (isHitRegisterPushNotif && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            RegisterPushNotificationWorker.scheduleWorker(this)
//        }
    }

    fun postLoginAction() {
        registerPushNotif()
        submitIntegrityApi()
        viewModel.updateSsoHostData(this, GotoSdk.LSDKINSTANCE?.getAccessToken() ?: "")
//        setTrackingUserId(userSession.userId)
//        setFCM()
        SubmitDeviceWorker.scheduleWorker(this, true)
        DataVisorWorker.scheduleWorker(this, true)
        TwoFactorMluHelper.clear2FaInterval(this)

        setResult(Activity.RESULT_OK)
        finish()
    }

    companion object {
        const val TOKOPEDIA_CARE_PATH = "help"
        const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
    }
}
