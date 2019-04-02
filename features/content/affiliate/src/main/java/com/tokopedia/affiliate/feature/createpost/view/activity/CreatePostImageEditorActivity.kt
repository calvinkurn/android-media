package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import java.util.*

/**
 * @author by yfsx on 26/09/18.
 */
class CreatePostImageEditorActivity : ImageEditorActivity() {
    companion object {

        private val DEFAULT_RESOLUTION = "100-square"
        private val RESOLUTION_300 = "300"
        private val PARAM_ID = "id"
        private val PARAM_WEB_SERVICE = "web_service"
        private val PARAM_RESOLUTION = "param_resolution"
        private val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"

        fun getInstance(context: Context, imageUrls: ArrayList<String>,
                        imageDescription: ArrayList<String>,
                        minResolution: Int,
                        @ImageEditActionTypeDef imageEditActionType: IntArray,
                        defaultRatio: ImageRatioTypeDef,
                        isCirclePreview: Boolean,
                        maxFileSize: Long,
                        ratioOptionList: ArrayList<ImageRatioTypeDef>?): Intent {
            val intent = Intent(context, CreatePostImageEditorActivity::class.java)
            intent.putExtra(ImageEditorActivity.EXTRA_IMAGE_URLS, imageUrls)
            intent.putExtra(ImageEditorActivity.EXTRA_IMAGE_DESCRIPTION_LIST, imageDescription)
            intent.putExtra(ImageEditorActivity.EXTRA_MIN_RESOLUTION, minResolution)
            intent.putExtra(ImageEditorActivity.EXTRA_EDIT_ACTION_TYPE, imageEditActionType)
            intent.putExtra(ImageEditorActivity.EXTRA_RATIO, defaultRatio)
            intent.putExtra(ImageEditorActivity.EXTRA_IS_CIRCLE_PREVIEW, isCirclePreview)
            intent.putExtra(ImageEditorActivity.EXTRA_MAX_FILE_SIZE, maxFileSize)
            intent.putExtra(ImageEditorActivity.EXTRA_RATIO_OPTION_LIST, ratioOptionList)
            return intent
        }
    }

}
