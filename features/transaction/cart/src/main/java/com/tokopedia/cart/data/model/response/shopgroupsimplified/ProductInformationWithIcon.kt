package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductInformationWithIcon(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("icon_url")
        val iconUrl: String = ""
)
