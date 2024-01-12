package com.tokopedia.stories.creation.view.model

import android.graphics.Bitmap

/**
 * Created By : Jonathan Darwin on October 25, 2023
 */
sealed interface StoriesMediaCover {

    object Loading : StoriesMediaCover

    data class Success(
        val localFilePath: String
    ) : StoriesMediaCover

    object Error : StoriesMediaCover
}
