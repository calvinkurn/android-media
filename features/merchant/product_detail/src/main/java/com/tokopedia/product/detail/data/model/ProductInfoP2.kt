package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.data.model.product.Rating
import com.tokopedia.product.detail.data.model.shop.ShopInfo

data class ProductInfoP2(
        var shopInfo: ShopInfo? = null,
        var isWishlisted: Boolean = false,
        var rating: Rating = Rating()
)