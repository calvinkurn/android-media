package com.tokopedia.updateinactivephone.revamp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.imagepicker.picker.main.adapter.ImagePickerViewPagerAdapter
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.revamp.common.cameraview.InactivePhoneImagePickerBuilder
import com.tokopedia.updateinactivephone.revamp.view.adapter.ImagePickerPagerAdapter

class InactivePhoneImagePickerActivity : ImagePickerActivity() {

    private var cameraViewMode: CameraViewMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent != null && intent.extras != null && intent.extras?.containsKey(EXTRA_IMAGE_PICKER_BUILDER) as Boolean) {
            val bundle = intent.getBundleExtra(EXTRA_IMAGE_PICKER_BUILDER)
            cameraViewMode = bundle?.getParcelable<CameraViewMode>(EXTRA_CAMERA_MODE) as CameraViewMode
        }

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getImagePickerViewPagerAdapter(): ImagePickerViewPagerAdapter {
        return ImagePickerPagerAdapter(this, supportFragmentManager, imagePickerBuilder, cameraViewMode)
    }

    companion object {
        private const val EXTRA_CAMERA_MODE = "cameraMode"

        fun createIntentCamera(context: Context?, cameraViewMode: CameraViewMode): Intent {
            val bundle = Bundle()
            val builder = InactivePhoneImagePickerBuilder(context?.getString(R.string.text_select_image) ?: "", intArrayOf(ImagePickerTabTypeDef.TYPE_CAMERA))
            val intent = Intent(context, InactivePhoneImagePickerActivity::class.java)
            bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, builder)
            bundle.putParcelable(EXTRA_CAMERA_MODE, cameraViewMode)
            intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle)
            return intent
        }

        fun createIntentCameraWithGallery(context: Context?, cameraViewMode: CameraViewMode): Intent {
            val bundle = Bundle()
            val builder = InactivePhoneImagePickerBuilder(context?.getString(R.string.text_select_image) ?: "", intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA))
            val intent = Intent(context, InactivePhoneImagePickerActivity::class.java)
            bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, builder)
            bundle.putParcelable(EXTRA_CAMERA_MODE, cameraViewMode)
            intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle)
            return intent
        }
    }
}