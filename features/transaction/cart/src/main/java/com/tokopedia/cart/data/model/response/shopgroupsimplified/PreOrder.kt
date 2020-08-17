package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class PreOrder(
        @SerializedName("is_preorder")
        val isPreorder: Boolean = false,
        @SerializedName("duration")
        val duration: String = ""
)