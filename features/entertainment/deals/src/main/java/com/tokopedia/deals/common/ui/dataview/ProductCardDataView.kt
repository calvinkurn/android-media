package com.tokopedia.deals.common.ui.dataview

data class ProductCardDataView(
        val id: String = "",
        val imageUrl: String = "",
        val title: String = "",
        val discount: String = "",
        val priceNonCurrency: String = "",
        val categoryName: String = "",
        val brand: String = "",
        val oldPrice: String = "",
        val price: String = "",
        val shop: String = "",
        val appUrl: String = "",
        val productCategory: ProductCategoryDataView? = null,
        val page: Int = 0,
        val position: Int = 0
) : DealsBaseItemDataView()