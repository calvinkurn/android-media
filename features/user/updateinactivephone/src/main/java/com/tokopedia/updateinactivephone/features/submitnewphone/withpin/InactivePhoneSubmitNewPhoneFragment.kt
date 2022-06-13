package com.tokopedia.updateinactivephone.features.submitnewphone.withpin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.IS_USE_REGULAR_FLOW
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.SOURCE_INACTIVE_PHONE
import com.tokopedia.updateinactivephone.domain.data.VerifyNewPhoneDataModel
import com.tokopedia.updateinactivephone.features.onboarding.regular.InactivePhoneRegularActivity
import com.tokopedia.updateinactivephone.features.onboarding.withpin.InactivePhoneWithPinActivity
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseInactivePhoneSubmitDataFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

open class InactivePhoneSubmitNewPhoneFragment : BaseInactivePhoneSubmitDataFragment() {

    override fun initObserver() {
        viewModel.phoneValidation.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    if (it.data.validation.isSuccess) {
                        onSuccessPhoneValidation()
                    } else {
                        onFailedPhoneValidation(MessageErrorException(it.data.validation.error))
                    }
                }
                is Fail -> {
                    onFailedPhoneValidation(it.throwable)
                }
            }
        })

        viewModel.submitDataExpedited.observe(viewLifecycleOwner, {
            hideLoading()
            when (it) {
                is Success -> {
                    if (it.data.submit.isSuccess == STATUS_OK) {
                        onSuccessSubmitNewPhoneNumber()
                    } else {
                        val errorMessage = it.data.submit.errorMessage.first()
                        onFailedSubmitNewPhoneNumber(MessageErrorException(errorMessage))
                    }
                }
                is Fail -> {
                    onFailedSubmitNewPhoneNumber(it.throwable)
                }
            }
        })

        viewModel.verifyNewPhone.observe(viewLifecycleOwner, {
            hideLoading()
            when (it) {
                is Success -> {
                    if (it.data.verify.isSuccess) {
                        onSuccessVerificationNewPhone(it.data)
                    } else {
                        onFailedVerificationNewPhone(MessageErrorException(it.data.verify.errorMessage))
                    }
                }
                is Fail -> {
                    onFailedVerificationNewPhone(it.throwable)
                }
            }
        })
    }

    override fun initView() {
        hideThumbnailLayout()

        viewBinding?.textPhoneNumber?.label = ""
        viewBinding?.buttonNext?.text = context?.getString(R.string.button_text_save)

        viewBinding?.textPhoneNumber?.apply {
            setOnClickListener {
                trackerRegular.clickOnTextViewInputNewPhoneNumber()
            }
            setAfterTextChangeListener {
                viewBinding?.buttonNext?.isEnabled = isPhoneValid()
            }
        }
    }

    override fun onSubmit() {
        hideKeyboard()
        trackerWithPin.clickOnButtonSubmitNewPhone()

        if (isPhoneValid()) {
            showLoading()

            inactivePhoneUserDataModel?.let {
                it.newPhoneNumber = viewBinding?.textPhoneNumber?.text.orEmpty()
                viewModel.userValidation(it)
            }
        } else {
            viewBinding?.buttonNext?.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_PHONE_VERIFICATION -> {
                hideLoading()
                if (resultCode == Activity.RESULT_OK) {
                    val isUseRegularFlow = data?.extras?.getBoolean(IS_USE_REGULAR_FLOW).orFalse()
                    inactivePhoneUserDataModel?.validateToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()

                    if (isUseRegularFlow) {
                        gotoRegularFlow()
                    } else {
                        onSuccessValidatePhoneNumber()
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        trackerWithPin.clickOnButtonBackAddNewPhone()
        dialogOnBackPressed()
        return true
    }

    private fun onSuccessPhoneValidation() {
        inactivePhoneUserDataModel?.let {
            viewModel.submitNewPhoneNumber(it)
        }
    }

    private fun onFailedPhoneValidation(throwable: Throwable) {
        hideLoading()

        val message = ErrorHandler.getErrorMessage(requireContext(), throwable)
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onSuccessSubmitNewPhoneNumber() {
        trackerWithPin.onSuccessSubmitNewPhone()
        gotoValidateNewPhoneNumber()
    }

    private fun onFailedSubmitNewPhoneNumber(throwable: Throwable) {
        trackerWithPin.onFailedSubmitNewPhone(throwable.message.toString())

        val message = ErrorHandler.getErrorMessage(context, throwable)
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onSuccessValidatePhoneNumber() {
        inactivePhoneUserDataModel?.let {
            showLoading()
            viewModel.verifyNewPhone(it)
        }
    }

    private fun onSuccessVerificationNewPhone(verifyNewPhoneDataModel: VerifyNewPhoneDataModel) {
        if (verifyNewPhoneDataModel.verify.isSuccess) {
            gotoSuccessPage(InactivePhoneConstant.EXPEDITED)
        }
    }

    private fun onFailedVerificationNewPhone(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(requireContext(), throwable)
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    open fun gotoValidateNewPhoneNumber() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_USER_ID_ENC,
            inactivePhoneUserDataModel?.userIdEnc.orEmpty()
        )
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_MSISDN,
            inactivePhoneUserDataModel?.newPhoneNumber.orEmpty()
        )
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN,
            inactivePhoneUserDataModel?.validateToken.orEmpty()
        )
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_SOURCE,
            SOURCE_INACTIVE_PHONE
        )
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_OTP_TYPE,
            InactivePhoneConstant.OTP_TYPE_INACTIVE_PHONE_SMS
        )
        startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION)
    }

    override fun dialogOnBackPressed() {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.text_exit_title))
                setDescription(getString(R.string.text_exit_description))
                setPrimaryCTAText(getString(R.string.text_exit_cta_primary))
                setSecondaryCTAText(getString(R.string.text_exit_cta_secondary))
                setPrimaryCTAClickListener {
                    this.dismiss()
                    trackerWithPin.clickOnPopupKeluar()
                    gotoOnboardingPage()
                }
                setSecondaryCTAClickListener {
                    trackerWithPin.clickOnPopupLanjutVerifikasi()
                    this.dismiss()
                }
                setCancelable(false)
                setOverlayClose(false)
            }.show()
        }
    }

    private fun gotoOnboardingPage() {
        activity?.let {
            inactivePhoneUserDataModel?.let { _inactivePhoneUserDataModel ->
                startActivity(
                    InactivePhoneWithPinActivity.createIntent(it, _inactivePhoneUserDataModel)
                )
            }

            it.finish()
        }
    }

    private fun gotoRegularFlow() {
        context?.let {
            inactivePhoneUserDataModel?.let { data ->
                val intent = InactivePhoneRegularActivity.createIntent(it, data)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    companion object {
        private const val STATUS_OK = 1
        private const val REQUEST_CODE_PHONE_VERIFICATION = 100

        fun create(bundle: Bundle): Fragment {
            return InactivePhoneSubmitNewPhoneFragment().apply {
                arguments = bundle
            }
        }
    }
}