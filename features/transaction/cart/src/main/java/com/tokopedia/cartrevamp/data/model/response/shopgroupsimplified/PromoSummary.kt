package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class PromoSummary(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("detail")
    val details: MutableList<PromoSummaryDetail> = arrayListOf()
)

data class PromoSummaryDetail(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("amount_str")
    val amountStr: String = "",
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("currency_details_str")
    val currencyDetailStr: String = ""
)
