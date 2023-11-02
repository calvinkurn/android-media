package com.tokopedia.stories.creation.view.model.state

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.view.model.StoriesMediaType
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.StoriesMedia

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
data class StoriesCreationUiState(
    val media: StoriesMedia,
    val config: StoriesCreationConfiguration,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
    val productTags: List<String>, /** TODO JOE: data class is not ready, will use proper data class later */
) {
    companion object {
        val Empty: StoriesCreationUiState
            get() = StoriesCreationUiState(
                media = StoriesMedia.Empty,
                config = StoriesCreationConfiguration.Empty,
                accountList = emptyList(),
                selectedAccount = ContentAccountUiModel.Empty,
                productTags = emptyList(),
            )
    }
}
