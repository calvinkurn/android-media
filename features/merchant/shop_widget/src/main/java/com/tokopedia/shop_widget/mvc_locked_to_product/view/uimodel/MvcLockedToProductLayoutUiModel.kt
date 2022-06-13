package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

data class MvcLockedToProductLayoutUiModel(
    val hasNextPage: Boolean = false,
    val mvcLockedToProductVoucherUiModel: MvcLockedToProductVoucherUiModel = MvcLockedToProductVoucherUiModel(),
    val mvcLockedToProductTotalProductAndSortUiModel: MvcLockedToProductSortSectionUiModel = MvcLockedToProductSortSectionUiModel(),
    val mvcLockedToProductListGridProductUiModel: List<MvcLockedToProductGridProductUiModel> = listOf(),
    val mvcLockedToProductErrorUiModel: MvcLockedToProductGlobalErrorUiModel = MvcLockedToProductGlobalErrorUiModel()
)