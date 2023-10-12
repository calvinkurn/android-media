package com.tokopedia.content.product.picker.seller.model.uimodel

import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel

/**
 * Created By : Jonathan Darwin on February 09, 2022
 */
data class PlayBroProductSummaryUiState(
    val productTagSectionList: List<ProductTagSectionUiModel>,
    val productCount: Int,
    val productTagSummary: ProductTagSummaryUiModel,
)

enum class ProductTagSummaryUiModel {
    Unknown,
    Loading,
    Success
}
