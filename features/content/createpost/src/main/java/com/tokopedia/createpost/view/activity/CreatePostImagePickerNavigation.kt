package com.tokopedia.createpost.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.viewmodel.MediaModel
import com.tokopedia.createpost.view.viewmodel.MediaType
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.putImagePickerBuilder


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
        val builder = ImagePickerBuilder.getSquareImageBuilder(context)
                .withSimpleEditor()
                .withSimpleMultipleSelection(imagePathList, maxImage)
                .apply {
                    title = context.getString(R.string.cp_title_image_picker)
                }
        return RouteManager.getIntent(context, ApplinkConstInternalGlobal.IMAGE_PICKER).apply {
            putImagePickerBuilder(builder)
        }
    }
}
