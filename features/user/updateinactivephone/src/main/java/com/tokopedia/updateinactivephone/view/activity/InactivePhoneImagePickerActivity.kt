package com.tokopedia.updateinactivephone.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.adapter.ImagePickerViewPagerAdapter
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.view.adapter.ImagePickerPagerAdapter

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
            val title = context?.getString(R.string.text_select_image) ?: ""
            val builder = ImagePickerBuilder(title, intArrayOf(ImagePickerTabTypeDef.TYPE_CAMERA), GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true, null, null)
            val intent = Intent(context, InactivePhoneImagePickerActivity::class.java)
            bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, builder)
            bundle.putParcelable(EXTRA_CAMERA_MODE, cameraViewMode)
            intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle)
            return intent
        }
    }
}