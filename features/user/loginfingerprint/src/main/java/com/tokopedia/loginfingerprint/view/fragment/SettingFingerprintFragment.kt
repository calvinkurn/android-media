package com.tokopedia.loginfingerprint.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintResult
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintResult
import com.tokopedia.loginfingerprint.databinding.FragmentSettingFingerprintBinding
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginfingerprint.tracker.BiometricTracker
import com.tokopedia.loginfingerprint.view.dialog.FingerprintDialogHelper
import com.tokopedia.loginfingerprint.view.helper.BiometricPromptHelper
import com.tokopedia.loginfingerprint.viewmodel.SettingFingerprintViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject


class SettingFingerprintFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(SettingFingerprintViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: BiometricTracker

    private var enableSwitch = true

    private val binding: FragmentSettingFingerprintBinding? by viewBinding()

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        getComponent(LoginFingerprintComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_fingerprint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(BiometricPromptHelper.isBiometricAvailable(activity)) {
            initObserver()
            loading()
            viewModel.getFingerprintStatus()
            binding?.fragmentFingerprintSettingSwitch?.setOnClickListener {
                if (enableSwitch) {
                    if (binding?.fragmentFingerprintSettingSwitch?.isChecked == true) {
                        enableSwitch = false
                        binding?.fragmentFingerprintSettingSwitch?.isChecked = false
                        enableSwitch = true
                        showBiometricPrompt()
                    } else {
                        viewModel.removeFingerprint()
                    }
                }
            }
        } else {
            FingerprintDialogHelper.showNotRegisteredFingerprintDialog(context, onPositiveButtonClick = {
                activity?.finish()
            }, onDismiss = { activity?.finish() })
        }
    }

    fun initObserver() {
        viewModel.checkFingerprintStatus.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> onSuccessGetFingerprintStatus(it.data)
                is Fail -> onFailedGetFingerprintStatus(it.throwable)
            }
            hideLoading()
        })

        viewModel.removeFingerprintResult.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    enableSwitch = false
                    binding?.fragmentFingerprintSettingSwitch?.isChecked = false
                    tracker.trackRemoveFingerprintSuccess()
                }
                is Fail -> {
                    enableSwitch = false
                    binding?.fragmentFingerprintSettingSwitch?.isChecked = true
                    tracker.trackRemoveFingerprintFailed(it.throwable.message ?: "")
                    showToaster(it.throwable.message)
                }
            }
            enableSwitch = true
        })

        viewModel.registerResultEvent.observe(viewLifecycleOwner) {
            if(it) {
                tracker.trackRegisterFpSuccess()
                enableSwitch = false
                binding?.fragmentFingerprintSettingSwitch?.isChecked = true
                view?.let { view ->
                    Toaster.build(view, getString(R.string.fingerprint_success_login_toaster), Toaster.LENGTH_LONG).show()
                }
            } else {
                enableSwitch = false
                binding?.fragmentFingerprintSettingSwitch?.isChecked = false
            }
            enableSwitch = true
            hideLoading()
        }

        viewModel.registerErrorEvent.observe(viewLifecycleOwner) {
            showToaster(it)
            tracker.trackRegisterFpFailed(it)
        }
    }

    private fun showToaster(message: String?) {
        if(context != null) {
            view?.let {
                Toaster.build(
                    it,
                    message ?: getString(R.string.error_default_fp),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    private fun goToVerification() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_SECURITY_QUESTION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SECURITY_QUESTION) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                viewModel.registerFingerprint()
            } else if(resultCode == Activity.RESULT_CANCELED) {
                enableSwitch = true
                onErrorRegisterFingerprint(Throwable(getString(R.string.error_failed_register_fingerprint)))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun onSuccessRegisterFingerprint(data: RegisterFingerprintResult) {
        if (data.success) {
            tracker.trackRegisterFpSuccess()
            enableSwitch = false
            binding?.fragmentFingerprintSettingSwitch?.isChecked = true
            view?.let {
                Toaster.build(it, getString(R.string.fingerprint_success_login_toaster), Toaster.LENGTH_LONG).show()
            }
        } else {
            tracker.trackRegisterFpFailed(data.errorMessage)
            onErrorRegisterFingerprint(Throwable(message = getString(R.string.error_failed_register_fingerprint)))
        }
        enableSwitch = true
    }

    fun onErrorRegisterFingerprint(throwable: Throwable) {
        tracker.trackRegisterFpFailed(throwable.message ?: "")
        showToaster(throwable.message)
    }

    fun onSuccessGetFingerprintStatus(checkFingerprintResponse: CheckFingerprintResult) {
        enableSwitch = false
        binding?.fragmentFingerprintSettingSwitch?.isChecked = checkFingerprintResponse.isRegistered
        enableSwitch = true
    }

    fun onFailedGetFingerprintStatus(throwable: Throwable) {
        showToaster(throwable.message)
    }

    fun loading() {
        binding?.fragmentFingerprintSettingContainer?.alpha = LESS_ALPHA
        binding?.fragmentFingerprintSettingLoader?.show()
    }

    fun hideLoading() {
        binding?.fragmentFingerprintSettingContainer?.alpha = NORMAL_ALPHA
        binding?.fragmentFingerprintSettingLoader?.hide()
    }

    private fun onSuccessAuthentication() {
        tracker.trackClickOnLoginWithFingerprintSuccessDevice()
        goToVerification()
    }

    private fun onFailedAuthentication() {
        tracker.trackClickOnLoginWithFingerprintFailedDevice("")
        enableSwitch = true
    }

    private fun onErrorAuthentication(errCode: Int, errString: String) {
        if(errCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
            tracker.trackButtonCloseVerify()
        } else {
            tracker.trackOpenVerifyFingerprintFailed(errString)
        }
        if(errCode == BiometricPrompt.ERROR_LOCKOUT) {
            FingerprintDialogHelper.showFingerprintLockoutDialog(activity)
        }
        enableSwitch = true
    }

    private fun showBiometricPrompt () {
        if(activity != null) {
            BiometricPromptHelper.showBiometricPromptFragment(requireActivity(), this,
                {
                    onSuccessAuthentication()
                }, {
                    onFailedAuthentication()
                }, { err, msg ->
                    onErrorAuthentication(err, msg)
                }
            )
        }
    }

    fun trackBackButton() {
        tracker.trackClickBtnBackAccountSetting()
    }

    companion object {
        const val TAG = "fingerprintSettingScreen"

        const val OTP_SECURITY_QUESTION = 158
        const val REQUEST_SECURITY_QUESTION = 104

        private const val NORMAL_ALPHA = 1.0F
        private const val LESS_ALPHA = 0.4f

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = SettingFingerprintFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}