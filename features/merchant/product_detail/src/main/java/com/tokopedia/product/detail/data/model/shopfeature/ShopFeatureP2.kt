package com.tokopedia.product.detail.data.model.shopfeature

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 21/07/20
 */
data class ShopFeatureP2(
        @SerializedName("IsGoApotik")
        @Expose
        val isGoApotik: Boolean = false
)