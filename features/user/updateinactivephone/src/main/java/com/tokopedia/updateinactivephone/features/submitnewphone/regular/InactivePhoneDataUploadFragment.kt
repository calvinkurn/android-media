package com.tokopedia.updateinactivephone.features.submitnewphone.regular

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ID_CARD
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.SELFIE
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.filePath
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.common.cameraview.FileType
import com.tokopedia.updateinactivephone.common.utils.getValidEmail
import com.tokopedia.updateinactivephone.common.view.ThumbnailFileView
import com.tokopedia.updateinactivephone.domain.usecase.SubmitDataModel
import com.tokopedia.updateinactivephone.features.imagepicker.InactivePhoneImagePickerActivity
import com.tokopedia.updateinactivephone.features.onboarding.regular.InactivePhoneRegularActivity
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseInactivePhoneSubmitDataFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

open class InactivePhoneDataUploadFragment : BaseInactivePhoneSubmitDataFragment() {

    private var idCardObj = ""
    private var selfieObj = ""

    override fun initView() {
        showThumbnailLayout()

        viewBinding?.imgIdCard?.apply {
            setImage(this, CameraViewMode.ID_CARD.id)
            setOnClickListener {
                val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
                startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD)
            }
        }

        viewBinding?.imgSelfie?.apply {
            setImage(this, CameraViewMode.SELFIE.id)
            setOnClickListener {
                val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.SELFIE)
                startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_SELFIE)
            }
        }

        viewBinding?.textPhoneNumber?.apply {
            setOnClickListener {
                trackerRegular.clickOnTextViewInputNewPhoneNumber()
            }
            setAfterTextChangeListener {
                viewBinding?.buttonNext?.isEnabled = isPhoneValid()
            }
        }
    }

    private fun goToVerification(phoneNumber: String) {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        intent.putExtra(
            ApplinkConstInternalGlobal.PARAM_OTP_TYPE,
            InactivePhoneConstant.SQCP_OTP_TYPE
        )
        startActivityForResult(intent, InactivePhoneConstant.REQUEST_SQCP_OTP_VERIFICATION)
    }

    override fun initObserver() {
        viewModel.phoneValidation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.validation.isSuccess) {
                        goToVerification(viewBinding?.textPhoneNumber?.text.orEmpty())
                    } else {
                        hideLoading()
                        onError(MessageErrorException(it.data.validation.error))
                    }
                }

                is Fail -> {
                    hideLoading()
                    onError(it.throwable)
                }
            }
        })

        viewModel.imageUpload.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (it.data.source == ID_CARD) {
                        idCardObj = it.data.data.pictureObject
                        doUploadImage(FileType.SELFIE, SELFIE)
                    } else if (it.data.source == SELFIE) {
                        selfieObj = it.data.data.pictureObject

                        inactivePhoneUserDataModel?.let { userData ->
                            userData.newPhoneNumber = viewBinding?.textPhoneNumber?.text.orEmpty()
                            viewModel.submitForm(SubmitDataModel(
                                email = userData.email,
                                oldPhone = userData.oldPhoneNumber,
                                newPhone = userData.newPhoneNumber,
                                userIndex = userData.userIndex,
                                idCardImage = idCardObj,
                                selfieImage = selfieObj,
                                validateToken = userData.validateToken
                            ))
                        }
                    }
                }

                is Fail -> {
                    hideLoading()
                    onError(it.throwable)
                }
            }
        })

        viewModel.submitData.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    if (it.data.status.isSuccess) {
                        gotoSuccessPage(InactivePhoneConstant.REGULAR)
                    } else {
                        view?.let { v ->
                            Toaster.build(v, it.data.status.errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                        }
                    }
                }
                is Fail -> {
                    if (it.throwable.message == getString(R.string.error_new_phone_already_registered)) {
                        viewBinding?.textPhoneNumber?.error = getString(R.string.error_input_another_phone)
                    } else {
                        onError(it.throwable)
                    }
                }
            }
        })
    }

    override fun onSubmit() {
        hideKeyboard()
        trackerRegular.clickOnButtonSubmitUploadData()

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

    override fun onFragmentBackPressed(): Boolean {
        trackerRegular.clickOnBackButtonUploadData()
        dialogOnBackPressed()
        return true
    }

    private fun doUploadImage(fileType: FileType, source: String) {
        context?.let {
            viewModel.uploadImage(
                inactivePhoneUserDataModel?.email?.getValidEmail().orEmpty(),
                inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty(),
                inactivePhoneUserDataModel?.userIndex.orZero(),
                filePath(it, fileType.id),
                source
            )
        }
    }

    private fun onError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(requireContext(), throwable)
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onOtpSuccess(validateToken: String) {
        inactivePhoneUserDataModel?.validateToken = validateToken
        doUploadImage(FileType.ID_CARD, ID_CARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                InactivePhoneConstant.REQUEST_SQCP_OTP_VERIFICATION -> {
                    if(data?.extras != null) {
                        val validateToken = data.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                        onOtpSuccess(validateToken)
                    }
                }
                InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD -> {
                    viewBinding?.imgIdCard?.let { setImage(it, CameraViewMode.ID_CARD.id) }
                }
                InactivePhoneConstant.REQUEST_CAPTURE_SELFIE -> {
                    viewBinding?.imgSelfie?.let { setImage(it, CameraViewMode.SELFIE.id) }
                }
            }
        }
    }

    private fun setImage(imageView: ThumbnailFileView, type: Int) {
        val path = context?.let { filePath(it, type) }
        if (path?.isNotEmpty() == true)
            imageView.apply {
                setImage(path)
            }
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
                    trackerRegular.clickOnExitButtonPopupUploadData()
                    gotoOnboardingPage()
                }
                setSecondaryCTAClickListener {
                    this.dismiss()
                }
                setCancelable(false)
                setOverlayClose(false)
            }.show()
        }
    }

    open fun gotoOnboardingPage() {
        activity?.let {
            inactivePhoneUserDataModel?.let { _inactivePhoneUserDataModel ->
                startActivity(InactivePhoneRegularActivity.createIntent(it, _inactivePhoneUserDataModel))
            }

            it.finish()
        }
    }

    override fun onDetach() {
        super.onDetach()
        removeFiles()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.phoneValidation.removeObservers(this)
        viewModel.imageUpload.removeObservers(this)
        viewModel.submitData.removeObservers(this)
    }

    companion object {
        fun create(bundle: Bundle): Fragment {
            return InactivePhoneDataUploadFragment().apply {
                arguments = bundle
            }
        }
    }
}