package com.tokopedia.updateinactivephone.features.submitnewphone.regular

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ID_CARD
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.SELFIE
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.STATUS_SUCCESS
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.filePath
import com.tokopedia.updateinactivephone.common.view.ThumbnailFileView
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.common.cameraview.FileType
import com.tokopedia.updateinactivephone.common.utils.getValidEmail
import com.tokopedia.updateinactivephone.features.imagepicker.InactivePhoneImagePickerActivity
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseInactivePhoneSubmitDataFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class InactivePhoneDataUploadFragment : BaseInactivePhoneSubmitDataFragment() {

    private var idCardObj = ""
    private var selfieObj = ""

    override fun initView() {
        super.initView()

        thumbnailIdCard?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD)
        }

        thumbnailSelfie?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.SELFIE)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_SELFIE)
        }

        textPhoneNumber?.setOnClickListener {
            tracker.clickOnTextViewInputNewPhoneNumber()
        }

        thumbnailIdCard?.let { setImage(it, CameraViewMode.ID_CARD.id) }
        thumbnailSelfie?.let { setImage(it, CameraViewMode.SELFIE.id) }

        showThumbnailLayout()
    }

    override fun initObserver() {
        viewModel.phoneValidation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.validation.status == STATUS_SUCCESS) {
                        doUploadImage(FileType.ID_CARD, ID_CARD)
                    }
                }

                is Fail -> {
                    hideLoading()
                    view?.let { view ->
                        Toaster.make(view, it.throwable.message.toString(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                    }
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
                        inactivePhoneUserDataModel?.newPhoneNumber = textPhoneNumber?.text.orEmpty()

                        viewModel.submitForm(
                            inactivePhoneUserDataModel?.email?.getValidEmail().orEmpty(),
                            inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty(),
                            inactivePhoneUserDataModel?.newPhoneNumber.orEmpty(),
                            inactivePhoneUserDataModel?.userIndex.orZero(),
                            idCardObj,
                            selfieObj
                        )
                    }
                }

                is Fail -> {
                    hideLoading()
                    view?.let { view ->
                        Toaster.make(view, it.throwable.message.toString(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                    }
                }
            }
        })

        viewModel.submitData.observe(this, Observer {
            hideLoading()
            when(it) {
                is Success -> {
                    gotoSuccessPage()
                }
                is Fail -> {
                    if (it.throwable.message == getString(R.string.error_new_phone_already_registered)) {
                        textPhoneNumber?.error = getString(R.string.error_input_another_phone)
                    } else {
                        view?.let { view ->
                            Toaster.make(view, it.throwable.message.toString(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
        })
    }

    override fun onSubmit() {
        hideKeyboard()

        if (isPhoneValid()) {
            showLoading()

            tracker.clickOnButtonSubmitUploadData()
            inactivePhoneUserDataModel?.newPhoneNumber = textPhoneNumber?.text.orEmpty()

            viewModel.userValidation(
                inactivePhoneUserDataModel?.oldPhoneNumber.orEmpty(),
                inactivePhoneUserDataModel?.email?.getValidEmail().orEmpty(),
                inactivePhoneUserDataModel?.userIndex.orZero()
            )
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD -> {
                    thumbnailIdCard?.let { setImage(it, CameraViewMode.ID_CARD.id) }
                }
                InactivePhoneConstant.REQUEST_CAPTURE_SELFIE -> {
                    thumbnailSelfie?.let { setImage(it, CameraViewMode.SELFIE.id) }
                }
            }
        }
    }

    private fun setImage(imageView: ThumbnailFileView, type: Int) {
        context?.let {
            val path = filePath(it, type)
            if (path.isNotEmpty())
                imageView.setImage(path)
        }
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