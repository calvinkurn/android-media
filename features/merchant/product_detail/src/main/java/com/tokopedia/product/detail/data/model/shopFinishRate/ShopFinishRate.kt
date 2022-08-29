package com.tokopedia.product.detail.data.model.shopFinishRate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 21/07/20
 */
data class ShopFinishRate(
        @SerializedName("finishRate")
        @Expose
        val finishRate: String = ""
)