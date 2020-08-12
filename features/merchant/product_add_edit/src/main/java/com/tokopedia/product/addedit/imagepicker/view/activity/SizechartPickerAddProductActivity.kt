package com.tokopedia.product.addedit.imagepicker.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.user.session.UserSession
import java.util.*

class SizechartPickerAddProductActivity : ImagePickerActivity() {
    var userSession: UserSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(context)
        isEdit = intent.getBooleanExtra(EXTRA_IS_EDIT, false)
    }

    override fun trackOpen() {
        // no-op
    }

    override fun trackContinue() {
        // no-op
    }

    override fun trackBack() {
        // no-op
    }

    override fun getEditorIntent(selectedImagePaths: ArrayList<String>): Intent {
        val targetIntent = Intent(this, SizechartPickerEditPhotoActivity::class.java)
        val origin = super.getEditorIntent(selectedImagePaths)

        origin.extras?.let {
            targetIntent.putExtras(it)
        }
        targetIntent.putExtra(EXTRA_IS_EDIT, isEdit)

        return targetIntent
    }

    companion object {
        private var isEdit = false
        const val EXTRA_IS_EDIT = "EXTRA_IS_EDIT"

        fun getIntent(context: Context, isEdit: Boolean?): Intent {
            val intent = Intent(context, SizechartPickerAddProductActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, createSizechartImagePickerBuilder(context))
            intent.putExtra(EXTRA_IS_EDIT, isEdit)
            intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle)
            return intent
        }

        private fun createSizechartImagePickerBuilder(context: Context): ImagePickerBuilder {
            val imagePickerEditorBuilder = ImagePickerEditorBuilder(intArrayOf(
                    ImageEditActionTypeDef.ACTION_BRIGHTNESS,
                    ImageEditActionTypeDef.ACTION_CONTRAST,
                    ImageEditActionTypeDef.ACTION_CROP,
                    ImageEditActionTypeDef.ACTION_ROTATE),
                    false,
                    null)

            return ImagePickerBuilder(context.getString(com.tokopedia.product.addedit.R.string.choose_image), intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY,
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
}