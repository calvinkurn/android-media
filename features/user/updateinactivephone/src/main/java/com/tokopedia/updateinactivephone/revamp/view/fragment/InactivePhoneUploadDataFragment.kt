package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.revamp.common.ThumbnailFileView
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneImagePickerActivity
import kotlinx.android.synthetic.main.fragment_inactive_phone_upload_data.*

class InactivePhoneUploadDataFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_upload_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            when (it.getString(KEY_SOURCE)) {
                InactivePhoneConstant.SELFIE -> {
                    imgSelfie?.visibility = View.VISIBLE
                    imgSavingBook?.visibility = View.GONE
                }
                InactivePhoneConstant.SAVING_BOOk -> {
                    imgSelfie?.visibility = View.GONE
                    imgSavingBook?.visibility = View.VISIBLE
                }
            }
        }

        btnNext?.setOnClickListener {
            textPhoneNumber?.error = "Nomor ini sudah terdaftar, masukan nomor lainnya"
        }

        imgIdCard?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD)
        }

        imgSelfie?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.SELFIE)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_SELFIE)
        }

        imgSavingBook?.setOnClickListener {
            val intent = InactivePhoneImagePickerActivity.createIntentCameraWithGallery(context, CameraViewMode.SAVING_BOOK)
            startActivityForResult(intent, InactivePhoneConstant.REQUEST_CAPTURE_SAVING_BOOK)
        }

        setImage(imgIdCard, CameraViewMode.ID_CARD.id)
        setImage(imgSelfie, CameraViewMode.SELFIE.id)
        setImage(imgSavingBook, CameraViewMode.SAVING_BOOK.id)
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
                InactivePhoneConstant.REQUEST_CAPTURE_SAVING_BOOK -> {
                    setImage(imgSavingBook, CameraViewMode.SAVING_BOOK.id)
                }
            }
        }
    }

    private fun setImage(imageView: ThumbnailFileView, type: Int) {
        context?.let {
            val path = InactivePhoneConstant.filePath(it, type)
            if (path.isNotEmpty())
                imageView.setImage(path)
        }
    }

    companion object {
        private const val KEY_SOURCE = "source"

        fun instance(source: String): InactivePhoneUploadDataFragment {
            val bundle = Bundle()
            val fragment = InactivePhoneUploadDataFragment()
            bundle.putString(KEY_SOURCE, source)
            fragment.arguments = bundle
            return fragment
        }
    }
}