package com.tokopedia.loginfingerprint.view.fragment

import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginfingerprint.listener.ScanFingerprintInterface
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginfingerprint.view.ScanFingerprintDialog
import com.tokopedia.loginfingerprint.viewmodel.RegisterOnboardingViewModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_register_fingerprint_onboarding.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RegisterFingerprintOnboardingFragment : BaseDaggerFragment(), ScanFingerprintInterface {

    private var scanFingerprintDialog: ScanFingerprintDialog? = null

    private var mainImage: ImageUnify? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @JvmField
    var cryptographyUtils: Cryptography? = null

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(RegisterOnboardingViewModel::class.java) }

    override fun onFingerprintValid() {
        showProgressBar()
        viewModel.registerFingerprint()
        hideFingerprintDialog()
    }

    override fun onLoginFingerprintSuccess() {}

    override fun onFingerprintError(msg: String, errCode: Int) {
        if(errCode == FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE ||
                errCode == FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS ||
                errCode == ScanFingerprintDialog.FP_ERROR_KEY_NOT_INITIALIZED){
            activity?.finish()
        }
        else onErrorRegisterFP(Throwable(message = msg))
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()

        fingerprint_onboarding_aktivasi_btn?.setOnClickListener {
            showFingerprintDialog()
        }

        fingerprint_onboarding_skip_btn?.setOnClickListener {
            viewModel.unregisterFP()
            activity?.finish()
        }

        setMainImage()

    }

    private fun setMainImage() {
        mainImage = activity?.findViewById(R.id.imageView2)
        (mainImage as ImageView).loadImage(imageUrl)
    }

    private fun setupObserver(){
        viewModel.verifyRegisterFingerprintResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessRegisterFP()
                is Fail -> onErrorRegisterFP(it.throwable)
            }
        })
    }

    private fun showFingerprintDialog(){
        activity?.run {
            if(scanFingerprintDialog == null)
                scanFingerprintDialog = ScanFingerprintDialog.newInstance(this, this@RegisterFingerprintOnboardingFragment)

            scanFingerprintDialog?.setCloseClickListener {
                hideFingerprintDialog()
            }
            scanFingerprintDialog?.showWithMode(ScanFingerprintDialog.MODE_REGISTER)
        }
    }

    override fun onDestroy() {
        hideFingerprintDialog()
        super.onDestroy()
    }

    override fun onPause() {
        hideFingerprintDialog()
        super.onPause()
    }

    private fun hideFingerprintDialog(){
        scanFingerprintDialog?.dismiss()
    }

    private fun onSuccessRegisterFP(){
        hideProgressBar()
        activity?.finish()
    }

    private fun onErrorRegisterFP(throwable: Throwable) {
        hideProgressBar()
    }

    private fun showProgressBar(){
        fingerprint_onboarding_aktivasi_btn?.hide()
        fingerprint_onboarding_skip_btn?.hide()
        fingerprint_onboarding_loader?.show()
    }

    private fun hideProgressBar(){
        fingerprint_onboarding_aktivasi_btn?.show()
        fingerprint_onboarding_skip_btn?.show()
        fingerprint_onboarding_loader?.hide()
    }

    companion object {
        private val imageUrl = "https://images.tokopedia.net/img/android/user/login_fingerprint_onboarding.png"
        fun newInstance(): RegisterFingerprintOnboardingFragment = RegisterFingerprintOnboardingFragment()
    }
}
