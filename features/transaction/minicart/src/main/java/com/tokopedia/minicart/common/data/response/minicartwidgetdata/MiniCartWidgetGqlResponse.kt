package com.tokopedia.minicart.common.data.response.minicartwidgetdata

import com.google.gson.annotations.SerializedName

data class MiniCartWidgetGqlResponse(
        @SerializedName("mini_cart")
        val miniCart: MiniCartData = MiniCartData()
)