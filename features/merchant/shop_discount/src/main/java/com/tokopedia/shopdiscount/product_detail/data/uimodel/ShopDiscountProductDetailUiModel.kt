package com.tokopedia.shopdiscount.product_detail.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailTypeFactoryImpl

data class ShopDiscountProductDetailUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val listProductDetailData: List<ProductDetailData> = listOf()
) {
    data class ProductDetailData(
        val productId: String = "",
        val productImageUrl: String = "",
        val productName: String = "",
        val minPrice: String = "",
        val maxPrice: String = "",
        val minPriceDiscounted: String = "",
        val maxPriceDiscounted: String = "",
        val minDiscount: String = "",
        val maxDiscount: String = "",
        val stock: String = "",
        val totalLocation: Int = 0,
        val startDate: String = "",
        val endDate: String = ""
    ) : Visitable<ShopDiscountProductDetailTypeFactoryImpl> {
        override fun type(typeFactory: ShopDiscountProductDetailTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }
}