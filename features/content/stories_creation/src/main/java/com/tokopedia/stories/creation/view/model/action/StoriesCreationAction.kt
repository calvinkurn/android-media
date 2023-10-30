package com.tokopedia.stories.creation.view.model.action

import com.tokopedia.stories.creation.view.model.StoriesMediaType

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
sealed interface StoriesCreationAction {

    data class SetMedia(
        val mediaFilePath: String,
        val mediaType: StoriesMediaType,
    ) : StoriesCreationAction

    object ClickAddProduct : StoriesCreationAction

    object ClickUpload : StoriesCreationAction
}
