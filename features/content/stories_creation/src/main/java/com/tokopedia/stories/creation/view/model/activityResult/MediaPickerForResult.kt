package com.tokopedia.stories.creation.view.model.activityResult

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.stories.creation.view.model.StoriesMedia

/**
 * Created By : Jonathan Darwin on October 31, 2023
 */
class MediaPickerForResult : ActivityResultContract<Intent, StoriesMedia>() {

    override fun createIntent(context: Context, input: Intent): Intent = input

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
