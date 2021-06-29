package com.tokopedia.loginfingerprint.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprint
import com.tokopedia.loginfingerprint.di.DaggerLoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintSettingModule
import com.tokopedia.loginfingerprint.view.dialog.FingerprintDialogHelper
import com.tokopedia.loginfingerprint.view.helper.BiometricPromptHelper
import com.tokopedia.loginfingerprint.viewmodel.FingerprintLandingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_verify_fingerprint_layout.*
import javax.inject.Inject

class VerifyFingerprintActivity: BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(FingerprintLandingViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_fingerprint_layout)
        initComponents()
        initObserver()
        if(BiometricPromptHelper.isBiometricAvailable(this)) {
            showBiometricPrompt()
        } else {
            FingerprintDialogHelper.showNotRegisteredFingerprintDialog(this, onPositiveButtonClick = {
                finishWithCanceled()
            }, onDismiss = { finishWithCanceled() })
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

    fun initObserver() {
        viewModel.verifyFingerprint.observe(this, androidx.lifecycle.Observer {
            when(it){
                is Success -> onSuccessVerifyFingerprint(it.data)
                is Fail -> onErrorVerifyFingerprint()
            }
        })
    }

    private fun onSuccessVerifyFingerprint(data: VerifyFingerprint) {
        if(data.isSuccess) {
            val intent = Intent().apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, data.validateToken)
            }
            setResult(Activity.RESULT_OK, intent)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
        finish()
    }

    private fun onErrorVerifyFingerprint() {
        fingerprint_landing_loader?.hide()
        finishWithCanceled()
    }

    private fun showBiometricPrompt() {
        BiometricPromptHelper.showBiometricPrompt(this,
            onSuccess = { verifyFingerprint() }, onFailed = {},
            onError = {
                if(it == BiometricPrompt.ERROR_LOCKOUT) {
                    FingerprintDialogHelper.showFingerprintLockoutDialog(this, onPositiveButtonClick = {
                        onErrorVerifyFingerprint()
                    }, onDismiss = { onErrorVerifyFingerprint() })
                } else {
                    onErrorVerifyFingerprint()
                }
            })
    }

    private fun verifyFingerprint() {
        fingerprint_landing_loader?.show()
        viewModel.verifyFingerprint()
    }
}