package com.tokopedia.stories.creation.view.model.state

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
data class StoriesCreationUiState(
    val mediaFilePath: String,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
) {
    companion object {
        val Empty: StoriesCreationUiState
            get() = StoriesCreationUiState(
//                mediaFilePath = "/storage/emulated/0/Movies/VID_20230905_113525.mp4",
                mediaFilePath = "",
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
                )
            )
    }
}
