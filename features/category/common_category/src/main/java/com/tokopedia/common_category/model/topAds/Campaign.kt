package com.tokopedia.common_category.model.topAds

import com.google.gson.annotations.SerializedName

data class Campaign(

        @field:SerializedName("original_price")
        val originalPrice: String? = null,

        @field:SerializedName("discount_percentage")
        val discountPercentage: Int? = null
)