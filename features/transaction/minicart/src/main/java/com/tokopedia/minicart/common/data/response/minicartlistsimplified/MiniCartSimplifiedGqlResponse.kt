package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class MiniCartSimplifiedGqlResponse(
        @SerializedName("mini_cart")
        val miniCart: MiniCartData = MiniCartData()
)