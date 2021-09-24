package com.tokopedia.updateinactivephone.features.submitnewphone.withpin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.SOURCE_INACTIVE_PHONE
import com.tokopedia.updateinactivephone.common.utils.getValidEmail
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseInactivePhoneSubmitDataFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

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
        viewModel.phoneValidation.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    gotoPhoneVerification()
                }
                is Fail -> {

                }
            }
        })

        viewModel.submitDataExpedited.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess) {
                        onSuccessSubmitNewPhoneNumber()
                    } else {
                        onFailedSubmitNewPhoneNumber(Throwable(it.data.errorMessage))
                    }
                }
                is Fail -> {
                    onFailedSubmitNewPhoneNumber(it.throwable)
                }
            }
        })
    }

    override fun onSubmit() {
        hideKeyboard()
        trackerWithPin.clickOnButtonSubmitNewPhone()

        if (isPhoneValid()) {
            showLoading()

            viewModel.userValidation(
                inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty(),
                inactivePhoneUserDataModel?.email?.getValidEmail().orEmpty(),
                inactivePhoneUserDataModel?.userIndex.orZero()
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_PHONE_VERIFICATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessVerificationNewPhone()
                } else {
                    onFailedVerificationNewPhone()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onSuccessSubmitNewPhoneNumber() {
        trackerWithPin.onSuccessSubmitNewPhone()
        gotoPhoneVerification()
    }

    private fun onFailedSubmitNewPhoneNumber(throwable: Throwable) {
        view?.let {
            Toaster.build(it, throwable.message.toString(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onSuccessVerificationNewPhone() {
        inactivePhoneUserDataModel?.newPhoneNumber = textPhoneNumber?.text.orEmpty()

        viewModel.submitNewPhoneNumber(
            inactivePhoneUserDataModel?.newPhoneNumber.orEmpty(),
            inactivePhoneUserDataModel?.email.orEmpty(),
            inactivePhoneUserDataModel?.userIndex.toString()
        )
    }

    private fun onFailedVerificationNewPhone() {

    }

    private fun gotoPhoneVerification() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, inactivePhoneUserDataModel?.newPhoneNumber.orEmpty())
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, inactivePhoneUserDataModel?.email?.getValidEmail().orEmpty())
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, SOURCE_INACTIVE_PHONE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, InactivePhoneConstant.OTP_TYPE_INACTIVE_PHONE_SMS)
        startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION)
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