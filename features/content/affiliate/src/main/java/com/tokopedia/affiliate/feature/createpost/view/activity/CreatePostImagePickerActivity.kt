package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.affiliate.R
import com.tokopedia.design.component.Dialog
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.*
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import java.util.*


/**
 * @author by yfsx on 26/09/18.
 */
class CreatePostImagePickerActivity : ImagePickerActivity() {

    override fun startEditorActivity(selectedImagePaths: ArrayList<String>) {
        val intent = getEditorIntent(selectedImagePaths)
        startActivityForResult(intent, ImagePickerActivity.REQUEST_CODE_EDITOR)
    }

    override fun getEditorIntent(selectedImagePaths: ArrayList<String>): Intent {
        return CreatePostImageEditorActivity.getInstance(this, selectedImagePaths,
                imageDescriptionList,
                imagePickerBuilder.minResolution, imagePickerBuilder.imageEditActionType,
                imagePickerBuilder.imageRatioTypeDef,
                imagePickerBuilder.isCirclePreview,
                imagePickerBuilder.maxFileSizeInKB,
                imagePickerBuilder.ratioOptionList)
    }

    override fun onDoneClicked() {
        if (intent.getBooleanExtra(ARGS_SHOW_WARNING, true)) {
            val dialog = Dialog(this, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.af_update_post))
            dialog.setDesc(getString(R.string.af_add_image_warning))
            dialog.setBtnOk(getString(R.string.af_continue))
            dialog.setBtnCancel(getString(R.string.cancel))
            dialog.setOnOkClickListener{
                dialog.dismiss()
                super.onDoneClicked()
            }
            dialog.setOnCancelClickListener{
                dialog.dismiss()
            }
            dialog.show()
        } else {
            super.onDoneClicked()
        }
    }

    companion object {
        private const val ARGS_SHOW_WARNING = "show_warning"

        fun getInstance(context: Context, selectedImageList: ArrayList<String>,
                        maxImage: Int, showWarningDialog: Boolean): Intent {
            val builder = ImagePickerBuilder(
                    context.getString(R.string.title_af_image_picker),
                    intArrayOf(TYPE_GALLERY, TYPE_CAMERA),
                    GalleryType.IMAGE_ONLY,
                    DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION,
                    ImageRatioTypeDef.RATIO_1_1,
                    true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE),
                            false,
                            null),
                    ImagePickerMultipleSelectionBuilder(selectedImageList, null, 0, maxImage))
            val intent = Intent(context, CreatePostImagePickerActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(ImagePickerActivity.EXTRA_IMAGE_PICKER_BUILDER, builder)
            intent.putExtra(ImagePickerActivity.EXTRA_IMAGE_PICKER_BUILDER, bundle)
            intent.putExtra(ARGS_SHOW_WARNING, showWarningDialog)
            return intent
        }
    }
}
