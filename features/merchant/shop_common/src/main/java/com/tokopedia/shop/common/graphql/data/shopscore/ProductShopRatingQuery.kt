package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 20/07/20
 */
data class ProductShopRatingQuery(
        @SerializedName("ratingScore")
        @Expose
        val ratingScore: Float = 0f
)