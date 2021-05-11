package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class UnavailableGroup(
        @SerializedName("cart_details")
        val cartDetails: List<CartDetail> = emptyList()
)