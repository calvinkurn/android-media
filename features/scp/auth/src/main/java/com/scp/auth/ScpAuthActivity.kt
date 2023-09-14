package com.scp.auth

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.scp.auth.di.DaggerScpAuthComponent
import com.scp.login.common.utils.LoginImageLoader
import com.scp.login.core.domain.common.UserCredential
import com.scp.login.core.domain.contracts.configs.LSdkChooseAccountUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkSsoUiConfigs
import com.scp.login.core.domain.contracts.configs.LSdkUiConfig
import com.scp.login.core.domain.contracts.listener.LSdkClientFlowListener
import com.scp.login.core.domain.contracts.listener.LSdkLoginFlowListener
import com.scp.login.core.domain.methods.mapper.MethodsUserNotExistFailure
import com.scp.verification.core.domain.common.entities.Failure
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiConstant
import com.tokopedia.devicefingerprint.integrityapi.IntegrityApiWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScpAuthActivity: BaseActivity() {

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
                ),
            ),
            additionalHeaders = TkpdAdditionalHeaders(this),
            loginSuccessListener = object : LSdkLoginFlowListener {
                override fun onLoginSuccessful() {
                    lifecycleScope.launch {
                        viewModel.getUserInfo()
                    }
                }

                override fun onLoginError(failure: Failure) {
                    if (failure is MethodsUserNotExistFailure) {
                        Toast.makeText(this@ScpAuthActivity, "User not exist, cred: ${failure.credentials}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onUserNotRegistered(credential: UserCredential) {

                }
            },
            clientFlowListener = object : LSdkClientFlowListener {
                override fun onHelpCentreClicked(screenType: String) {
                }

                override fun onAccountRecoverClicked() {

                }

                override fun onTermsServicesClicked() {
                    Toast.makeText(this@ScpAuthActivity, "On Terms Services Click", Toast.LENGTH_SHORT).show()
                }

                override fun onPrivacyPolicyClicked() {
                    Toast.makeText(this@ScpAuthActivity, "On Privacy Policy Clicked", Toast.LENGTH_SHORT).show()
                }

                override fun onLanguageSelectorClicked() {
                    TODO("Not yet implemented")
                }
            },
            imageLoader = object: LoginImageLoader {
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
}
