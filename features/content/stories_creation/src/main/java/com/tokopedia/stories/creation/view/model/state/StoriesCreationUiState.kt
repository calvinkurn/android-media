package com.tokopedia.stories.creation.view.model.state

import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
data class StoriesCreationUiState(
    val mediaFilePath: String,
    val mediaType: ContentMediaType,
    val config: StoriesCreationConfiguration,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
    val productTags: List<ProductTagSectionUiModel>,
) {
    companion object {
        val Empty: StoriesCreationUiState
            get() = StoriesCreationUiState(
                mediaFilePath = "",
                mediaType = ContentMediaType.Unknown,
                config = StoriesCreationConfiguration.Empty,
                accountList = emptyList(),
                selectedAccount = ContentAccountUiModel.Empty,
                productTags = emptyList(),
            )
    }
}
