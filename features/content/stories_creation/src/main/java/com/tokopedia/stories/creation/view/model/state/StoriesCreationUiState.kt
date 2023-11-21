package com.tokopedia.stories.creation.view.model.state

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.stories.creation.view.factory.StoriesMediaFactory
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
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
    val productTags: List<ProductTagSectionUiModel>,
) {

    val productList: List<ProductUiModel>
        get() = productTags.flatMap { it.products }

    companion object {
        val Empty: StoriesCreationUiState
            get() = StoriesCreationUiState(
                media = StoriesMediaFactory.get(),
                config = StoriesCreationConfiguration.Empty,
                accountList = emptyList(),
                selectedAccount = ContentAccountUiModel.Empty,
                productTags = emptyList(),
            )
    }
}
