package com.tokopedia.stories.creation.view.model.action

import com.tokopedia.creation.common.upload.model.ContentMediaType

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
sealed interface StoriesCreationAction {

    object Prepare : StoriesCreationAction

    data class SetMedia(
        val mediaFilePath: String,
        val mediaType: ContentMediaType,
    ) : StoriesCreationAction

    object ClickUpload : StoriesCreationAction
}
