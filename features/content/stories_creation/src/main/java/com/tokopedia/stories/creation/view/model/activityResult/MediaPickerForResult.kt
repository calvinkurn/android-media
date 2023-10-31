package com.tokopedia.stories.creation.view.model.activityResult

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.stories.creation.view.model.StoriesMediaType

/**
 * Created By : Jonathan Darwin on October 31, 2023
 */
class MediaPickerForResult : ActivityResultContract<Intent, StoriesMedia>() {

    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): StoriesMedia {
        return if (intent != null) {
            val mediaData = MediaPicker.result(intent)
            val mediaFilePath = mediaData.editedPaths.firstOrNull() ?: mediaData.originalPaths.firstOrNull().orEmpty()
            val mediaType = StoriesMediaType.parse(mediaFilePath)

            StoriesMedia(mediaFilePath, mediaType)
        } else {
            StoriesMedia.Empty
        }
    }
}
