package com.tokopedia.stories.creation.view.model.activityResult

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.util.device.ContentDeviceUtil
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.stories.creation.util.firstNotEmptyOrNull
import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.stories.creation.R as storiescreationR

/**
 * Created By : Jonathan Darwin on October 31, 2023
 */
class MediaPickerForResult : ActivityResultContract<MediaPickerIntentData, StoriesMedia>() {

    override fun createIntent(context: Context, input: MediaPickerIntentData): Intent {
        val pickerParam: PickerParam.() -> Unit = {
            pageSource(PageSource.Stories)
            minVideoDuration(input.minVideoDuration)
            maxVideoDuration(input.maxVideoDuration)
            pageType(PageType.COMMON)
            modeType(ModeType.COMMON)
            singleSelectionMode()
            withEditor {
                setCustomCtaText(context.getString(storiescreationR.string.stories_creation_next))
            }
            withImmersiveEditor {
                videoFileResultAppendix = input.storiesId
            }
            previewActionText(input.previewActionText)
        }

        return if (GlobalConfig.DEBUG && ContentDeviceUtil.isProbablyEmulator) {
            MediaPicker.intent(context, pickerParam)
        } else {
            MediaPicker.intentWithGalleryFirst(context, pickerParam)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): StoriesMedia {
        return if (intent != null) {
            val mediaData = MediaPicker.result(intent)
            val mediaFilePath = mediaData.editedPaths.firstNotEmptyOrNull()
                            ?: mediaData.editedImages.firstNotEmptyOrNull()
                            ?: mediaData.originalPaths.firstNotEmptyOrNull().orEmpty()
            val mediaType = ContentMediaType.parse(mediaFilePath)

            StoriesMedia(mediaFilePath, mediaType)
        } else {
            StoriesMedia.Empty
        }
    }
}
