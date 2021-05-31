package com.tokopedia.updateinactivephone.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ID_CARD
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.SELFIE
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.STATUS_SUCCESS
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.filePath
import com.tokopedia.updateinactivephone.common.view.ThumbnailFileView
import com.tokopedia.updateinactivephone.common.UserDataTemporary
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.common.cameraview.FileType
import com.tokopedia.updateinactivephone.di.DaggerInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.view.InactivePhoneTracker
import com.tokopedia.updateinactivephone.view.activity.InactivePhoneImagePickerActivity
import com.tokopedia.updateinactivephone.view.activity.InactivePhoneSuccessPageActivity
import com.tokopedia.updateinactivephone.view.viewmodel.InactivePhoneDataUploadViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_inactive_phone_data_upload.*
import javax.inject.Inject

class InactivePhoneDataUploadFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userDataTemp: UserDataTemporary

    @Inject
    lateinit var tracker: InactivePhoneTracker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(InactivePhoneDataUploadViewModel::class.java) }

    private var idCardObj = ""
    private var selfieObj = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerInactivePhoneComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .inactivePhoneModule(InactivePhoneModule(requireContext()))
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_data_upload, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext?.setOnClickListener {
            showLoading()
            if (!isPhoneValid()) {
                hideLoading()
                return@setOnClickListener
            }

            tracker.clickOnButtonSubmitUploadData()
            viewModel.userValidation(userDataTemp.getOldPhone(), userDataTemp.getEmail(), userDataTemp.getIndex())
        }

        imgIdCard?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD)
        }

        imgSelfie?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.SELFIE)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_SELFIE)
        }

        textPhoneNumber?.setOnClickListener {
            tracker.clickOnTextViewInputNewPhoneNumber()
        }

        setImage(imgIdCard, CameraViewMode.ID_CARD.id)
        setImage(imgSelfie, CameraViewMode.SELFIE.id)
    }

    private fun initObserver() {
        viewModel.phoneValidation.observe(this, Observer {
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
                        textPhoneNumber?.let { newPhone ->
                            viewModel.submitForm(userDataTemp.getEmail(), userDataTemp.getOldPhone(), newPhone.text, userDataTemp.getIndex(), idCardObj, selfieObj)
                        }
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

    private fun doUploadImage(fileType: FileType, source: String) {
        context?.let {
            viewModel.uploadImage(userDataTemp.getEmail(), userDataTemp.getOldPhone(), userDataTemp.getIndex(), filePath(it, fileType.id), source)
        }
    }

    private fun gotoSuccessPage() {
        textPhoneNumber?.let {
            userDataTemp.setNewPhone(it.text)
        }

        activity?.let {
            val intent = InactivePhoneSuccessPageActivity.createIntent(it)
            startActivity(intent)
            it.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD -> {
                    setImage(imgIdCard, CameraViewMode.ID_CARD.id)
                }
                InactivePhoneConstant.REQUEST_CAPTURE_SELFIE -> {
                    setImage(imgSelfie, CameraViewMode.SELFIE.id)
                }
            }
        }
    }

    private fun isPhoneValid(): Boolean {
        val phoneNumber = textPhoneNumber?.text.toString()
        when {
            phoneNumber.isEmpty() -> {
                textPhoneNumber?.error = getString(R.string.text_form_error_empty)
                return false
            }
            phoneNumber.length < 9 -> {
                textPhoneNumber?.error = getString(R.string.text_form_error_min_9_digit)
                return false
            }
        }

        return true
    }

    private fun setImage(imageView: ThumbnailFileView, type: Int) {
        context?.let {
            val path = filePath(it, type)
            if (path.isNotEmpty())
                imageView.setImage(path)
        }
    }

    private fun showLoading() {
        btnNext?.isEnabled = false
        imgIdCard?.isEnabled = false
        imgSelfie?.isEnabled = false
        textPhoneNumber?.isEnabled = false
        loader?.show()
    }

    private fun hideLoading() {
        btnNext?.isEnabled = true
        imgIdCard?.isEnabled = true
        imgSelfie?.isEnabled = true
        textPhoneNumber?.isEnabled = true
        loader?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
        viewModel.phoneValidation.removeObservers(this)
        viewModel.imageUpload.removeObservers(this)
        viewModel.submitData.removeObservers(this)
    }
}