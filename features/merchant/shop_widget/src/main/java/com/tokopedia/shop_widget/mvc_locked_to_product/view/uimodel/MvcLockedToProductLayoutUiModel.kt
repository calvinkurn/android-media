package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

data class MvcLockedToProductLayoutUiModel(
    val hasNextPage: Boolean = false,
    val mvcLockedToProductVoucherUiModel: MvcLockedToProductVoucherUiModel = MvcLockedToProductVoucherUiModel(),
    val mvcLockedToProductTotalProductAndSortUiModel: MvcLockedToProductTotalProductAndSortUiModel = MvcLockedToProductTotalProductAndSortUiModel(),
    val mvcLockedToListProductGridProductUiModel: List<MvcLockedToProductGridProductUiModel> = listOf()
)