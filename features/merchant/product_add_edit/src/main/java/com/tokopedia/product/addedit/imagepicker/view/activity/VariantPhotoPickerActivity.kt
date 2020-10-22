package com.tokopedia.product.addedit.imagepicker.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import java.util.*

class VariantPhotoPickerActivity : ImagePickerActivity() {

    private var isEdit = false

    companion object {

        const val EXTRA_IS_EDIT = "EXTRA_IS_EDIT"

        fun getIntent(context: Context?, isEdit: Boolean): Intent? {
            val intent = Intent(context, VariantPhotoPickerActivity::class.java)
            val bundle = Bundle()
            context?.run { bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, createVariantPhotoPickerBuilder(context)) }
            intent.putExtra(EXTRA_IS_EDIT, isEdit)
            intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle)
            return intent
        }

        private fun createVariantPhotoPickerBuilder(context: Context): ImagePickerBuilder? {
            val imagePickerEditorBuilder = ImagePickerEditorBuilder(intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS,
                    ImageEditActionTypeDef.ACTION_CONTRAST,
                    ImageEditActionTypeDef.ACTION_CROP,
                    ImageEditActionTypeDef.ACTION_ROTATE),
                    false,
                    null)
            return ImagePickerBuilder(context.getString(com.tokopedia.imagepicker.R.string.choose_image), intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY,
                    ImagePickerTabTypeDef.TYPE_CAMERA,
                    ImagePickerTabTypeDef.TYPE_INSTAGRAM),
                    GalleryType.IMAGE_ONLY,
                    ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                    ImageRatioTypeDef.RATIO_1_1,
                    true,
                    imagePickerEditorBuilder,
                    null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(EXTRA_IS_EDIT)) {
            isEdit = intent.getBooleanExtra(EXTRA_IS_EDIT, false)
        }
    }

    override fun getEditorIntent(selectedImagePaths: ArrayList<String?>?): Intent? {
        val targetIntent = Intent(this, VariantPhotoEditorActivity::class.java)
        val origin: Intent = super.getEditorIntent(selectedImagePaths)
        origin.extras?.let { targetIntent.putExtras(it) }
        targetIntent.putExtra(EXTRA_IS_EDIT, isEdit)
        return targetIntent
    }
}