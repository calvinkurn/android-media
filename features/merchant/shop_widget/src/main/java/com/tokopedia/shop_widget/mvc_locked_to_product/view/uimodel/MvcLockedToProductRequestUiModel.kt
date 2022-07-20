package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class MvcLockedToProductRequestUiModel(
    val shopID: String = "",
    val promoID: String = "",
    val page: Int = 0,
    val perPage: Int = 0,
    val selectedSortData: MvcLockedToProductSortUiModel,
    val localCacheModel: LocalCacheModel = LocalCacheModel()
)