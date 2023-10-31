package com.tokopedia.stories.creation.view.model.action

import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
sealed interface StoriesCreationAction {

    object Prepare : StoriesCreationAction

    data class SetMedia(
        val media: StoriesMedia
    ) : StoriesCreationAction

    data class SetProduct(
        val productTags: List<ProductTagSectionUiModel>,
    ) : StoriesCreationAction

    object ClickUpload : StoriesCreationAction
}
