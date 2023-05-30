package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class AddOns(
        @SerializedName("addon_id")
        val addonId: Long = 0L,
        @SerializedName("status")
        val status: Int = -1,
        @SerializedName("type")
        val type: Int = -1
)
