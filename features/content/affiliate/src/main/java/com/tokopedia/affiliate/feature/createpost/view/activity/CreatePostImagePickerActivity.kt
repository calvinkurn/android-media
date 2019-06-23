package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.MediaModel
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.MediaType
import com.tokopedia.design.component.Dialog
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.*
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.*
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import java.util.*
import kotlin.collections.ArrayList


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
                imagePickerBuilder.minResolution,
                imagePickerBuilder.imageEditActionType,
                imagePickerBuilder.imageRatioTypeDef,
                imagePickerBuilder.isCirclePreview,
                imagePickerBuilder.maxFileSizeInKB,
                imagePickerBuilder.ratioOptionList)
    }

    override fun onDoneClicked() {
        val isImageExist = intent?.getBooleanExtra(
                CreatePostImagePickerActivity.VIDEO_EXIST,
                false)?: false

        if (isImageExist) {
            val dialog = Dialog(this, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.af_title_update_post))
            dialog.setDesc(
                    getString(R.string.af_message_update_post,
                    getString(R.string.af_title_photo)))
            dialog.setBtnCancel(getString(R.string.cancel))
            dialog.setBtnOk(getString(R.string.af_continue))
            dialog.setOnOkClickListener{
                dialog.dismiss()
                super.onDoneClicked()
            }
            dialog.setOnCancelClickListener{
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
        } else {
            super.onDoneClicked()
        }
    }

    companion object {
        private const val ARGS_SHOW_WARNING = "show_warning"
        private const val VIDEO_EXIST = "video_exist"

        fun getInstance(context: Context, selectedImageList: ArrayList<MediaModel>,
                        maxImage: Int, showWarningDialog: Boolean): Intent {

            //showing only image type
            val imagePathList = ArrayList(
                    selectedImageList
                    .filter { it.type == MediaType.IMAGE }
                    .toList()
                    .map { it.path })

            val builder = ImagePickerBuilder(
                    context.getString(R.string.title_af_image_picker),
                    intArrayOf(TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM),
                    GalleryType.IMAGE_ONLY,
                    DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION,
                    ImageRatioTypeDef.RATIO_1_1,
                    true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE),
                            false,
                            null),
                    ImagePickerMultipleSelectionBuilder(imagePathList, null, 0, maxImage))
            val intent = Intent(context, CreatePostImagePickerActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(ImagePickerActivity.EXTRA_IMAGE_PICKER_BUILDER, builder)
            intent.putExtra(ImagePickerActivity.EXTRA_IMAGE_PICKER_BUILDER, bundle)
            intent.putExtra(CreatePostImagePickerActivity.VIDEO_EXIST, selectedImageList.isNotEmpty())
            intent.putExtra(ARGS_SHOW_WARNING, showWarningDialog)
            return intent
        }
    }
}
