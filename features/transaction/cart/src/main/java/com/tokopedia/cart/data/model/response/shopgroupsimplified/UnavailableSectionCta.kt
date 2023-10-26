package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class UnavailableSectionCta(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("message")
    val message: String = ""
)
