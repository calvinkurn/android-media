package com.tokopedia.stories.creation.view.model.state

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
data class StoriesCreationUiState(
    val mediaFilePath: String,
    val storiesId: String,
    val config: StoriesManualConfiguration,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
) {
    companion object {
        val Empty: StoriesCreationUiState
            get() = StoriesCreationUiState(
                mediaFilePath = "",
                storiesId = "",
                config = StoriesManualConfiguration.Empty,
                accountList = listOf(
                    ContentAccountUiModel(
                        id = "123",
                        name = "Jonathan Darwin",
                        iconUrl = "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
                        type = "seller",
                        hasUsername = true,
                        hasAcceptTnc = true,
                        badge = "",
                        enable = true,
                    )
                ),
                selectedAccount = ContentAccountUiModel(
                    id = "123",
                    name = "Jonathan Darwin",
                    iconUrl = "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
                    type = "seller",
                    hasUsername = true,
                    hasAcceptTnc = true,
                    badge = "",
                    enable = true,
                ),
            )
    }
}


data class StoriesManualConfiguration(
    val maxProductTag: Int,
    val showDuration: String,
    val maxStories: MaxStories,
) {

    data class MaxStories(
        val isLimitReached: Boolean,
        val imageUrl: String,
        val title: String,
        val description: String,
        val primaryText: String,
        val secondaryText: String,
    ) {
        companion object {
            val Empty: MaxStories
                get() = MaxStories(
                    isLimitReached = false,
                    imageUrl = "",
                    title = "",
                    description = "",
                    primaryText = "",
                    secondaryText = "",
                )
        }
    }

    companion object {
        val Empty: StoriesManualConfiguration
            get() = StoriesManualConfiguration(
                maxProductTag = 0,
                showDuration = "24 Jam",
                maxStories = MaxStories.Empty,
            )
    }
}
