package com.tokopedia.content.product.preview.viewmodel.state

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel

data class ProductPreviewUiState(
    val tabsUiModel: ProductPreviewTabUiModel = ProductPreviewTabUiModel.Empty,
    val productUiModel: ProductUiModel = ProductUiModel.Empty,
    val reviewUiModel: ReviewUiModel = ReviewUiModel.Empty,
    val bottomNavUiModel: BottomNavUiModel = BottomNavUiModel.Empty
)
