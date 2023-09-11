package com.tokopedia.stories.creation.view.model.action

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
sealed interface StoriesCreationAction {

    object Prepare : StoriesCreationAction

    data class SetMedia(
        val mediaFilePath: String,
    ) : StoriesCreationAction

    data class ClickAddProduct(
        val productTags: List<String>,
    ) : StoriesCreationAction

    object ClickUpload : StoriesCreationAction
}
