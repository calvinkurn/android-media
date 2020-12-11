package com.tokopedia.createpost.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.viewmodel.MediaModel
import com.tokopedia.createpost.view.viewmodel.MediaType
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


/**
 * @author by yfsx on 26/09/18.
 */
object CreatePostImagePickerNavigation {
    fun getIntent(context: Context, selectedImageList: ArrayList<MediaModel>, maxImage: Int): Intent {

        //showing only image type
        val imagePathList = ArrayList(
                selectedImageList
                .filter { it.type == MediaType.IMAGE }
                .toList()
                .map { it.path })

        /** Hide IG Since It's not working right now */
        val builder = ImagePickerBuilder(
                context.getString(R.string.cp_title_image_picker),
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
                ImagePickerMultipleSelectionBuilder(imagePathList, null, 0, maxImage))

        return ImagePickerActivity.getIntent(context, builder)
    }
}
