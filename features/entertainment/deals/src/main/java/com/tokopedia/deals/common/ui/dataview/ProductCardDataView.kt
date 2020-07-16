package com.tokopedia.deals.common.ui.dataview

data class ProductCardDataView(
    val id: String = "",
    val imageUrl: String = "",
    val title: String = "",
    val discount: String = "",
    val oldPrice: String = "",
    val price: String = "",
    val shop: String = "",
    val appUrl: String = "",
    val productCategory: ProductCategoryDataView? = null
)