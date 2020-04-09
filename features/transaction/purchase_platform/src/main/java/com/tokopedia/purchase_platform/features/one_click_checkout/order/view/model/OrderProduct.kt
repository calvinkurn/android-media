package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.purchase_platform.common.data.model.response.WholesalePrice
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProductDataResponse

data class OrderProduct(
        var parentId: Int = 0,
        var productId: Int = 0,
        var productName: String = "",
        var productPrice: Long = 0,
        var wholesalePrice: List<WholesalePrice> = arrayListOf(),
        var productImageUrl: String = "",
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var originalPrice: Long = productPrice,
        var discountedPercentage: Float = 0f,
        var isFreeOngkir: Boolean = false,
        var freeOngkirImg: String = "",
        var weight: Int = 0,
        var quantity: QuantityUiModel = QuantityUiModel(),
        var notes: String = "",
        var cashback: String = "",
        var productResponse: ProductDataResponse = ProductDataResponse()
)
