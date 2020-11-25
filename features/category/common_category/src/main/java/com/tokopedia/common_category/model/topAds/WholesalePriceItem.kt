package com.tokopedia.common_category.model.topAds

import com.google.gson.annotations.SerializedName

data class WholesalePriceItem(

        @field:SerializedName("price_format")
        val priceFormat: String? = null,

        @field:SerializedName("quantity_max_format")
        val quantityMaxFormat: String? = null,

        @field:SerializedName("quantity_min_format")
        val quantityMinFormat: String? = null
)