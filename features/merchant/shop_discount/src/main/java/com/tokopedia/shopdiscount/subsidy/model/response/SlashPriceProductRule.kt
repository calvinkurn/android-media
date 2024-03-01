package com.tokopedia.shopdiscount.subsidy.model.response

import com.google.gson.annotations.SerializedName

data class SlashPriceProductRule(
    @SerializedName("able_to_opt_out")
    val isAbleToOptOut: Boolean = false,
)
