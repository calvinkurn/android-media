package com.tokopedia.stories.creation.view.model.state

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
data class StoriesCreationUiState(
    val mediaFilePath: String,
    val config: StoriesCreationConfiguration,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
    val productTags: List<String>, /** TODO JOE: data class is not ready, will use proper data class later */
) {
    companion object {
        val Empty: StoriesCreationUiState
            get() = StoriesCreationUiState(
                mediaFilePath = "",
                config = StoriesCreationConfiguration.Empty,
                accountList = emptyList(),
                selectedAccount = ContentAccountUiModel.Empty,
                productTags = emptyList(),
            )
    }
}
