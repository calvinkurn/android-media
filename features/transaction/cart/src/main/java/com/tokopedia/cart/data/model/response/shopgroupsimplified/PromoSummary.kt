package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 17/02/2021
 */
data class PromoSummary(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("detail")
        @Expose
        val details: MutableList<PromoSummaryDetail> = arrayListOf()
)

data class PromoSummaryDetail(
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("amount_str")
        @Expose
        val amountStr: String = "",
        @SerializedName("amount")
        @Expose
        val amount: Double = 0.0,
        @SerializedName("currency_details_str")
        @Expose
        val currencyDetailStr: String = ""
)