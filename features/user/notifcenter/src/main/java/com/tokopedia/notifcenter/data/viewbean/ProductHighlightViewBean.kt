package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.notifcenter.data.entity.Shop

class ProductHighlightViewBean(
        val id: Int,
        val name: String,
        val imageUrl: String,
        val price: String,
        val priceInt: Int,
        val isStockEmpty: Boolean,
        val freeOngkirIcon: String,
        val isFreeOngkir: Boolean,
        var originalPrice: String,
        var discountPercentage: Int,
        val shop: Shop?
)