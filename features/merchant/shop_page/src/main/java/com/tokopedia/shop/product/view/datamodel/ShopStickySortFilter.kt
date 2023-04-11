package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.shop.sort.view.model.ShopProductSortModel

data class ShopStickySortFilter(
    val etalaseList: List<ShopEtalaseItemDataModel> = listOf(),
    val sortList: List<ShopProductSortModel> = listOf()
)
