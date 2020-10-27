package com.tokopedia.updateinactivephone.revamp.view.fragment

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
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ID_CARD
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_EMAIL
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_PHONE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_USER_ID
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.SELFIE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.filePath
import com.tokopedia.updateinactivephone.revamp.common.ThumbnailFileView
import com.tokopedia.updateinactivephone.revamp.common.UserDataTemporary
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.revamp.common.cameraview.FileType
import com.tokopedia.updateinactivephone.revamp.di.DaggerInactivePhoneComponent
import com.tokopedia.updateinactivephone.revamp.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneImagePickerActivity
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneSuccessPageActivity
import com.tokopedia.updateinactivephone.revamp.view.viewmodel.InactivePhoneDataUploadViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_inactive_phone_data_upload.*
import javax.inject.Inject

class InactivePhoneDataUploadFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userDataTemp: UserDataTemporary

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(InactivePhoneDataUploadViewModel::class.java) }

    private var uploadHost = ""
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
        initObserver()
        return inflater.inflate(R.layout.fragment_inactive_phone_data_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext?.setOnClickListener {
            if (!isPhoneValid()) {
                return@setOnClickListener
            }

            viewModel.userValidation(userDataTemp.getOldPhone(), userDataTemp.getIndex())
        }

        imgIdCard?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD)
        }

        imgSelfie?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.SELFIE)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_SELFIE)
        }

        setImage(imgIdCard, CameraViewMode.ID_CARD.id)
        setImage(imgSelfie, CameraViewMode.SELFIE.id)
    }

    private fun initObserver() {
        viewModel.phoneValidation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.validation.isSuccess && it.data.validation.status == 1) {
                        viewModel.getUploadHost()
                    } else {
                        // message if status not success / 1
                    }
                }

                is Fail -> {

                }
            }
        })

        viewModel.uploadHost.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    uploadHost = it.data.data.generatedHost.uploadHost
                    doUploadImage(FileType.ID_CARD, ID_CARD)
                }

                is Fail -> {

                }
            }
        })

        viewModel.imageUpload.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.source == ID_CARD) {
                        idCardObj = it.data.picObj
                        doUploadImage(FileType.SELFIE, SELFIE)
                    } else if (it.data.source == SELFIE) {
                        selfieObj = it.data.picObj
                        textPhoneNumber?.let { newPhone ->
                            viewModel.submitForm(userDataTemp.getEmail(), newPhone.text, userDataTemp.getIndex(), idCardObj, selfieObj)
                        }
                    }
                }

                is Fail -> {

                }
            }
        })

        viewModel.submitData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    gotoSuccessPage()
                }
                is Fail -> {
                    if (it.throwable.message == getString(R.string.error_new_phone_already_registered)) {
                        textPhoneNumber?.error = getString(R.string.error_input_another_phone)
                    } else {

                    }
                }
            }
        })
    }

    private fun doUploadImage(fileType: FileType, source: String) {
        context?.let {
            viewModel.uploadImage(uploadHost, userDataTemp.getUserId(), filePath(it, fileType.id), source)
        }
    }

    private fun gotoSuccessPage() {
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
}