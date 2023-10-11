package com.tokopedia.content.product.picker.sgc.model.uimodel

import com.tokopedia.content.product.picker.sgc.model.campaign.ProductTagSectionUiModel

/**
 * Created By : Jonathan Darwin on February 09, 2022
 */
data class PlayBroProductSummaryUiState(
    val productTagSectionList: List<ProductTagSectionUiModel>,
    val productCount: Int,
    val productTagSummary: ProductTagSummaryUiModel,
)

sealed class ProductTagSummaryUiModel {
    object Unknown: ProductTagSummaryUiModel()
    object Loading: ProductTagSummaryUiModel()
    object Success: ProductTagSummaryUiModel()
}
