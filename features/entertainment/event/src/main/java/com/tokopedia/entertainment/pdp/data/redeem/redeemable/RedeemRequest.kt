package com.tokopedia.entertainment.pdp.data.redeem.redeemable

import com.google.gson.annotations.SerializedName

data class RedeemRequest(
    @SerializedName("redemption_ids")
    val listIds: List<Int> = emptyList()
)
