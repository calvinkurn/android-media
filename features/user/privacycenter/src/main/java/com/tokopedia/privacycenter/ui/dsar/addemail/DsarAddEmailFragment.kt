package com.tokopedia.privacycenter.ui.dsar.addemail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.FragmentDsarAddEmailLayoutBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.ui.dsar.DsarConstants
import com.tokopedia.privacycenter.ui.dsar.uimodel.AddEmailModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DsarAddEmailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentDsarAddEmailLayoutBinding>()

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            DsarAddEmailViewModel::class.java
        )
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDsarAddEmailLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.imgIllustration?.loadImage(getString(R.string.dsar_add_email_illustration))
        setupObserver()
        binding?.btnAddEmail?.setOnClickListener {
            val email = binding?.txtFieldEmail?.editText?.text?.toString()?.trim() ?: ""
            viewModel.checkEmail(email)
        }
    }

    private fun renderViewModel(data: AddEmailModel) {
        data.let {
            binding?.btnAddEmail?.isLoading = it.btnLoading
            if (it.inputError.isNotEmpty()) {
                binding?.txtFieldEmail?.isInputError = true
                binding?.txtFieldEmail?.editText?.error = it.inputError
            } else {
                binding?.txtFieldEmail?.isInputError = false
            }
        }
    }

    private fun setupObserver() {
        viewModel.addEmailModel.observe(viewLifecycleOwner) {
            renderViewModel(it)
        }

        viewModel.routeToSuccessPage.observe(viewLifecycleOwner) {
            finishActivityWithResultOk()
        }

        viewModel.routeToVerification.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                goToVerificationActivity(it)
            }
        }
    }

    private fun finishActivityWithResultOk() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_EMAIL && resultCode == Activity.RESULT_OK) {
            onSuccessVerifyAddEmail(data)
        }
    }

    private fun goToVerificationActivity(email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtras(
            Bundle().apply {
                putString(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
                putString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
                putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_ADD_EMAIL)
                putString(
                    ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE,
                    DsarConstants.OTP_MODE_EMAIL
                )
                putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
            }
        )
        startActivityForResult(intent, REQUEST_ADD_EMAIL)
    }

    private fun onSuccessVerifyAddEmail(data: Intent?) {
        data?.extras?.run {
            val otpCode = getString(ApplinkConstInternalGlobal.PARAM_OTP_CODE, "")
            val validateToken = getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
            val email = binding?.txtFieldEmail?.editText?.text.toString().trim()
            viewModel.addEmail(email, otpCode, validateToken)
        }
    }

    companion object {
        fun newInstance() = DsarAddEmailFragment()

        private const val REQUEST_ADD_EMAIL = 100
        private const val OTP_TYPE_ADD_EMAIL = 141
    }
}
