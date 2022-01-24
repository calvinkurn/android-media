package com.tokopedia.loginfingerprint.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintResult
import com.tokopedia.loginfingerprint.di.DaggerLoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintSettingModule
import com.tokopedia.loginfingerprint.view.dialog.FingerprintDialogHelper
import com.tokopedia.loginfingerprint.view.fragment.SettingFingerprintFragment
import com.tokopedia.loginfingerprint.view.helper.BiometricPromptHelper
import com.tokopedia.loginfingerprint.viewmodel.SettingFingerprintViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class RegisterFingerprintActivity: BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(SettingFingerprintViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_fingerprint_layout)
        initComponents()
        initObserver()
        if(BiometricPromptHelper.isBiometricAvailable(this)) {
            showBiometricPrompt()
        } else {
            finish()
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun initObserver() {
        viewModel.registerFingerprintResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessRegisterFingerprint(it.data)
                is Fail -> {
                    onErrorRegisterFingerprint(throwable = it.throwable)
                }
            }
        })
    }

    fun onSuccessRegisterFingerprint(data: RegisterFingerprintResult) {
        if (data.success) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            onErrorRegisterFingerprint(Throwable(message = getString(R.string.error_failed_register_fingerprint)))
        }
    }

    fun onErrorRegisterFingerprint(throwable: Throwable) {
        finishWithCanceled()
    }

    fun finishWithCanceled() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun initComponents() {
        DaggerLoginFingerprintComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .loginFingerprintSettingModule(LoginFingerprintSettingModule(this))
            .build()
            .inject(this)
    }

    private fun showBiometricPrompt () {
        BiometricPromptHelper.showBiometricPromptActivity(this,
            {
                onSuccessAuthentication()
            }, {
                onFailedAuthentication()
            }, { err, msg ->
                onErrorAuthentication(err, msg)
            }
        )
    }

    private fun onErrorAuthentication(errCode: Int, errString: String) {
        if(errCode == BiometricPrompt.ERROR_LOCKOUT) {
            FingerprintDialogHelper.showFingerprintLockoutDialog(this)
        }
    }

    private fun onSuccessAuthentication() {
        goToVerification()
    }

    private fun onFailedAuthentication() {

    }

    private fun goToVerification() {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_OTP_TYPE,
            SettingFingerprintFragment.OTP_SECURITY_QUESTION
        )
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        startActivityForResult(intent, SettingFingerprintFragment.REQUEST_SECURITY_QUESTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SECURITY_QUESTION) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                viewModel.registerFingerprint()
            } else if(resultCode == Activity.RESULT_CANCELED) {
                onErrorRegisterFingerprint(Throwable(getString(R.string.error_failed_register_fingerprint)))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    companion object {
        const val REQUEST_SECURITY_QUESTION = 104
    }
}