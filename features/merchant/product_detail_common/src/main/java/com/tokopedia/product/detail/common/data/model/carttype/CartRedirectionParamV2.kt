package com.tokopedia.product.detail.common.data.model.carttype

import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 07/07/20
 */
data class CartRedirectionParamV2(
        @SerializedName("flag")
        val flag: String = "",
        @SerializedName("value")
        val value: String = ""
)