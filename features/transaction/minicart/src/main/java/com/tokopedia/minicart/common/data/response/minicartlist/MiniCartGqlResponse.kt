package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class MiniCartGqlResponse(
    @SerializedName("mini_cart_v3")
    val miniCart: MiniCartData = MiniCartData()
)
