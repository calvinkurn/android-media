package com.tokopedia.stories.creation.view.model.state

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
data class StoriesCreationUiState(
    val mediaFilePath: String,
) {
    companion object {
        val Empty: StoriesCreationUiState
            get() = StoriesCreationUiState(
                mediaFilePath = ""
            )
    }
}
