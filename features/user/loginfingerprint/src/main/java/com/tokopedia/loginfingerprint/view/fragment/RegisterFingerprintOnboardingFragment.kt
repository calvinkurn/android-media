package com.tokopedia.loginfingerprint.view.fragment

import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginfingerprint.listener.ScanFingerprintInterface
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginfingerprint.view.ScanFingerprintDialog
import com.tokopedia.loginfingerprint.viewmodel.RegisterOnboardingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_register_fingerprint_onboarding.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RegisterFingerprintOnboardingFragment : BaseDaggerFragment() {

    val TAG = "RegisterFingerprintFragment"

    private var scanFingerprintDialog: ScanFingerprintDialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @JvmField
    var cryptographyUtils: Cryptography? = null

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(RegisterOnboardingViewModel::class.java) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(LoginFingerprintComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_fingerprint_onboarding, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        cryptographyUtils = CryptographyUtils()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()

        fingerprint_onboarding_aktivasi_btn.setOnClickListener {
            showFingerprintDialog()
        }

        fingerprint_onboarding_skip_btn.setOnClickListener {
            viewModel.unregisterFP()
            activity?.finish()
        }
    }

    private fun setupObserver(){
        viewModel.verifyRegisterFingerprintResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessRegisterFP()
                is Fail -> onErrorRegisterFP(it.throwable)
            }
        })
    }

    private fun showFingerprintDialog(){
        scanFingerprintDialog = ScanFingerprintDialog(activity!!, object: ScanFingerprintInterface {
            override fun onFingerprintValid() {
                showProgressBar()
                viewModel.registerFingerprint()
                scanFingerprintDialog?.dismiss()
            }

            override fun onLoginFingerprintSuccess() {
                // do nothing
            }

            override fun onFingerprintError(msg: String, errCode: Int) {
                if(errCode == FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE || errCode == FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS){
                    activity?.finish()
                }else{
                    onErrorRegisterFP(Throwable(message = msg))
                }
            }
        })

        scanFingerprintDialog?.setCloseClickListener {
            scanFingerprintDialog?.dismiss()
        }
        scanFingerprintDialog?.showWithMode(ScanFingerprintDialog.MODE_REGISTER)
    }

    private fun onSuccessRegisterFP(){
        hideProgressBar()
        activity?.finish()
    }

    private fun onErrorRegisterFP(throwable: Throwable) {
        hideProgressBar()
    }

    private fun showProgressBar(){
        fingerprint_onboarding_aktivasi_btn.visibility = View.GONE
        fingerprint_onboarding_skip_btn.visibility = View.GONE
        fingerprint_onboarding_loader.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        fingerprint_onboarding_aktivasi_btn.visibility = View.VISIBLE
        fingerprint_onboarding_skip_btn.visibility = View.VISIBLE
        fingerprint_onboarding_loader.visibility = View.GONE
    }
}
