package com.tokopedia.notifcenter.data.viewbean

class ProductHighlightViewBean(
        val id: Int,
        val name: String,
        val imageUrl: String,
        val price: String,
        val isStockEmpty: Boolean,
        val freeOngkirIcon: String,
        val isFreeOngkir: Boolean,
        var originalPrice: String,
        var discountPercentage: Int
)