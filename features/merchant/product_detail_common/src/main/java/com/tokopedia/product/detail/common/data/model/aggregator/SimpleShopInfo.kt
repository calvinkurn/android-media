package com.tokopedia.product.detail.common.data.model.aggregator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 07/07/21
 */
data class SimpleShopInfo(
        @SerializedName("shopType")
        @Expose
        val shopType: String = ""
)