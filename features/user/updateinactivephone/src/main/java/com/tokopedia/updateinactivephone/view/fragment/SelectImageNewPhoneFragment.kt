package com.tokopedia.updateinactivephone.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants

import java.io.File

import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneAnalytics
import com.tokopedia.updateinactivephone.di.component.DaggerUpdateInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule
import javax.inject.Inject

class SelectImageNewPhoneFragment : BaseDaggerFragment() {
    private var idPhotoViewRelativeLayout: RelativeLayout? = null
    private var accountViewRelativeLayout: RelativeLayout? = null
    private var idPhotoView: ImageView? = null
    private var accountPhotoView: ImageView? = null

    private var uploadIdPhoto: ImageView? = null
    private var uploadPaymentPhoto: ImageView? = null
    private var continueButton: TextView? = null
    private var selectImageInterface: SelectImageInterface? = null

    private var photoIdPath: String? = null
    private var accountIdPath: String? = null
    private var isValidPhotoPath = false

    @Inject
    lateinit var analytics: UpdateInactivePhoneAnalytics

    override fun onStart() {
        super.onStart()
        analytics.screen(screenName)
        analytics.eventViewPhotoUploadScreen()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        try {
            selectImageInterface = context as SelectImageInterface
        } catch (e: Exception) {

        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            val paymentIdImagePath = savedInstanceState.getString(PAYMENT_ID_IMAGE_PATH)
            val photoIdImagePath = savedInstanceState.getString(PHOTO_ID_IMAGE_PATH)

            if (!TextUtils.isEmpty(photoIdImagePath)) {
                selectImageInterface?.setPhotoIdImagePath(photoIdImagePath)
                loadImageToImageView(idPhotoView, photoIdImagePath)
            }

            if (!TextUtils.isEmpty(paymentIdImagePath)) {
                selectImageInterface?.setAccountPhotoImagePath(paymentIdImagePath)
                loadImageToImageView(accountPhotoView, paymentIdImagePath)
            }

        }

    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_change_inactive_form_request, parent, false)

        uploadIdPhoto = view.findViewById(R.id.upload_id_photo_button)
        uploadPaymentPhoto = view.findViewById(R.id.upload_account_book_button)
        continueButton = view.findViewById(R.id.button_submit)
        idPhotoViewRelativeLayout = view.findViewById(R.id.upload_id_photo_view)
        idPhotoView = view.findViewById(R.id.photo_id)

        accountViewRelativeLayout = view.findViewById(R.id.upload_account_book_view)
        accountPhotoView = view.findViewById(R.id.account_book)

        prepareView()
        return view
    }

    private fun prepareView() {
        uploadPaymentPhoto?.setOnClickListener(onUploadAccountBook())
        uploadIdPhoto?.setOnClickListener(onUploadPhotoId())
        accountViewRelativeLayout?.setOnClickListener(onUploadAccountBook())
        idPhotoViewRelativeLayout?.setOnClickListener(onUploadPhotoId())

        continueButton?.setOnClickListener {
            analytics.eventClickPhotoProceed()
            analytics.eventClickButtonNext()
            selectImageInterface?.onContinueButtonClick()
            analytics.eventSuccessClickButtonNext()
        }
    }

    private fun onUploadPhotoId(): View.OnClickListener {
        return View.OnClickListener {
            val builder = getImagePickerBuilder(getString(R.string.foto_ktp_title))
            val intent = ImagePickerActivity.getIntent(activity, builder)
            startActivityForResult(intent, REQUEST_CODE_PHOTO_ID)
        }
    }

    private fun onUploadAccountBook(): View.OnClickListener {
        return View.OnClickListener {
            val builder = getImagePickerBuilder(getString(R.string.foto_payment_title))
            val intent = ImagePickerActivity.getIntent(activity, builder)
            startActivityForResult(intent, REQUEST_CODE_PAYMENT_PROOF)
        }
    }

    private fun getImagePickerBuilder(title: String): ImagePickerBuilder {
        return ImagePickerBuilder(title,
                intArrayOf(ImagePickerTabTypeDef.TYPE_CAMERA), GalleryType.IMAGE_ONLY, MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, null, true, null, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_PHOTO_ID -> if (resultCode == Activity.RESULT_OK && data != null) {
                val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                    val imagePath = imageUrlOrPathList[0]
                    uploadIdPhoto?.visibility = View.GONE
                    idPhotoViewRelativeLayout?.visibility = View.VISIBLE
                    photoIdPath = imagePath
                    loadImageToImageView(idPhotoView, imagePath)
                    selectImageInterface?.setPhotoIdImagePath(imagePath)

                    val bundle = Bundle()
                    bundle.putString(PAYMENT_ID_IMAGE_PATH, accountIdPath)
                    bundle.putString(PHOTO_ID_IMAGE_PATH, photoIdPath)

                    onSaveInstanceState(bundle)
                }
            }
            REQUEST_CODE_PAYMENT_PROOF -> if (resultCode == Activity.RESULT_OK && data != null) {
                val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                    val imagePath = imageUrlOrPathList[0]
                    uploadPaymentPhoto?.visibility = View.GONE
                    accountViewRelativeLayout?.visibility = View.VISIBLE
                    accountIdPath = imagePath
                    loadImageToImageView(accountPhotoView, imagePath)
                    selectImageInterface?.setAccountPhotoImagePath(imagePath)

                    val bundle = Bundle()
                    bundle.putString(PAYMENT_ID_IMAGE_PATH, accountIdPath)
                    bundle.putString(PHOTO_ID_IMAGE_PATH, photoIdPath)

                    onSaveInstanceState(bundle)
                }
            }
        }
        setSubmitButton()
    }

    private fun setSubmitButton() {
        checkImageNotEmpty()
        if (isValidPhotoPath) {
            MethodChecker.setBackground(continueButton,
                    MethodChecker.getDrawable(activity,
                            R.drawable.green_button_rounded
                    ))
            continueButton?.setTextColor(MethodChecker.getColor(activity,
                    R.color.white))
            continueButton?.isClickable = true
            continueButton?.isEnabled = true
        } else {
            continueButton?.isClickable = false
            continueButton?.isEnabled = false
            continueButton?.setTextColor(resources.getColor(R.color.black_26))
        }
    }

    private fun checkImageNotEmpty(){
        if(photoIdPath != null && accountIdPath != null) {
            isValidPhotoPath = true
        }
    }

    private fun loadImageToImageView(imageView: ImageView?, imagePath: String?) {
        ImageHandler.loadImageFromFile(activity, imageView, File(imagePath))
    }

    override fun getScreenName(): String {
        return UpdateInactivePhoneEventConstants.SELECT_IMAGE_TO_UPLOAD
    }

    override fun initInjector() {
        DaggerUpdateInactivePhoneComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .updateInactivePhoneModule(UpdateInactivePhoneModule(requireContext()))
                .build().inject(this)
    }

    interface SelectImageInterface {
        fun onContinueButtonClick()

        fun setAccountPhotoImagePath(imagePath: String?)

        fun setPhotoIdImagePath(imagePath: String?)

    }

    companion object {

        private val REQUEST_CODE_PHOTO_ID = 1001
        private val MAX_IMAGE_SIZE_IN_KB = 10 * 1024 * 1024
        private val REQUEST_CODE_PAYMENT_PROOF = 2001
        private val PAYMENT_ID_IMAGE_PATH = "paymentIdImagePath"
        private val PHOTO_ID_IMAGE_PATH = "photoIdImagePath"

        val instance: Fragment
            get() = SelectImageNewPhoneFragment()
    }
}
