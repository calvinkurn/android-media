package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel

/**
 * Created By : Jonathan Darwin on February 09, 2022
 */
data class PlayBroProductSummaryUiState(
    val productTagSummary: ProductTagSummaryUiModel
)

sealed class ProductTagSummaryUiModel {
    object Unknown: ProductTagSummaryUiModel()
    object Loading: ProductTagSummaryUiModel()
    object LoadingWithPlaceholder: ProductTagSummaryUiModel()
    data class Success(val sections: List<ProductTagSectionUiModel>, val productCount: Int): ProductTagSummaryUiModel()
    object Empty: ProductTagSummaryUiModel()
}