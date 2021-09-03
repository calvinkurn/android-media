package com.tokopedia.updateinactivephone.features.submitnewphone.withpin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.SOURCE_INACTIVE_PHONE
import com.tokopedia.updateinactivephone.common.utils.getValidEmail
import com.tokopedia.updateinactivephone.features.onboarding.withpin.InactivePhoneOnboardingPinFragment
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseInactivePhoneSubmitDataFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class InactivePhoneSubmitNewPhoneFragment : BaseInactivePhoneSubmitDataFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideThumbnailLayout()
    }

    override fun initView() {
        super.initView()

        textPhoneNumber?.label = ""
        buttonSubmit?.text = context?.getString(R.string.button_text_save)
    }

    override fun initObserver() {
        viewModel.submitDataExpedited.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    onSuccessSubmitNewPhoneNumber()
                }
                is Fail -> {
                    onFailedSubmitNewPhoneNumber()
                }
            }
        })
    }

    override fun onSubmit() {
        hideKeyboard()

        if (isPhoneValid()) {
            showLoading()

            inactivePhoneUserDataModel?.newPhoneNumber = textPhoneNumber?.text.orEmpty()

            viewModel.submitNewPhoneNumber(
                inactivePhoneUserDataModel?.newPhoneNumber.orEmpty(),
                inactivePhoneUserDataModel?.email.orEmpty()
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_PHONE_VERIFICATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    gotoSuccessPage()
                } else {

                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onSuccessSubmitNewPhoneNumber() {
        gotoPhoneVerification()
    }

    private fun onFailedSubmitNewPhoneNumber() {

    }

    private fun gotoPhoneVerification() {
        val intent = goToVerification(InactivePhoneConstant.OTP_TYPE_VERIFY_NEW_PHONE)
        startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION)
    }

    private fun goToVerification(otpType: Int): Intent {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, inactivePhoneUserDataModel?.newPhoneNumber.orEmpty())
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, inactivePhoneUserDataModel?.email?.getValidEmail().orEmpty())
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, SOURCE_INACTIVE_PHONE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType)
        return intent
    }

    companion object {
        private const val REQUEST_CODE_PHONE_VERIFICATION = 100

        fun create(bundle: Bundle): Fragment {
            return InactivePhoneSubmitNewPhoneFragment().apply {
                arguments = bundle
            }
        }
    }
}