package com.tokopedia.loginfingerprint.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginfingerprint.view.helper.BiometricPromptHelper
import com.tokopedia.loginfingerprint.viewmodel.SettingFingerprintViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_setting_fingerprint.*
import javax.inject.Inject


class SettingFingerprintFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(SettingFingerprintViewModel::class.java) }

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
        initObserver()
        loading()
        viewModel.getFingerprintStatus()

        fragment_fingerprint_setting_switch?.setOnCheckedChangeListener { switch, isEnable ->
            if(isEnable) {
                switch.isChecked = false
                showBiometricPrompt()
            } else {
                // remove fingerprint api
            }
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

        viewModel.registerFingerprintResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    fragment_fingerprint_setting_switch?.isChecked = true
//                    activity?.finish()
                }
                is Fail -> {
                    fragment_fingerprint_setting_switch?.isChecked = false
                    NetworkErrorHelper.showRedSnackbar(activity, it.throwable.message)
                }
            }
            hideLoading()
        })
    }

    fun onSuccessGetFingerprintStatus(checkFingerprintResponse: CheckFingerprintPojo) {
        fragment_fingerprint_setting_switch?.isChecked = checkFingerprintResponse.data.isRegistered
    }

    fun onFailedGetFingerprintStatus(throwable: Throwable) {
        activity?.let {
            NetworkErrorHelper.showSnackbar(activity, throwable.message)
        }
    }

    fun loading() {
        fragment_fingerprint_setting_container?.alpha = 0.5F
        fragment_fingerprint_setting_loader.show()
    }

    fun hideLoading() {
        fragment_fingerprint_setting_container?.alpha = 1.0F
        fragment_fingerprint_setting_loader.hide()
    }

    fun showBiometricPrompt () {
        BiometricPromptHelper.showBiometricPrompt(this,
            onSuccess = {
                loading()
                viewModel.registerFingerprint()
            },
            onFailed = {}, onError = {})
    }

    companion object {
        const val TAG = "fingerprintSettingScreen"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = SettingFingerprintFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}