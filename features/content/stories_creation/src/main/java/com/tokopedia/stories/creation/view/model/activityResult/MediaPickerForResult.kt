package com.tokopedia.stories.creation.view.model.activityResult

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.stories.creation.R
import com.tokopedia.stories.creation.view.model.StoriesMedia

/**
 * Created By : Jonathan Darwin on October 31, 2023
 */
class MediaPickerForResult : ActivityResultContract<MediaPickerIntentData, StoriesMedia>() {

    override fun createIntent(context: Context, input: MediaPickerIntentData): Intent {
        return MediaPicker.intent(context) {
            pageSource(PageSource.Stories)
            minVideoDuration(input.minVideoDuration)
            maxVideoDuration(input.maxVideoDuration)
            pageType(PageType.COMMON)
            modeType(ModeType.COMMON)
            singleSelectionMode()
            withImmersiveEditor()
            previewActionText(input.previewActionText)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): StoriesMedia {
        return if (intent != null) {
            val mediaData = MediaPicker.result(intent)
            val mediaFilePath = mediaData.editedPaths.firstOrNull() ?: mediaData.originalPaths.firstOrNull().orEmpty()
            val mediaType = ContentMediaType.parse(mediaFilePath)

            StoriesMedia(mediaFilePath, mediaType)
        } else {
            StoriesMedia.Empty
        }
    }
}
