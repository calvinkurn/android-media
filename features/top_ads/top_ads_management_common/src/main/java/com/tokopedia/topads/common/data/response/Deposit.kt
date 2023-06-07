package com.tokopedia.topads.common.data.response


import com.google.gson.annotations.SerializedName

data class Deposit(
    @SerializedName("topadsDashboardDepositsV2")
    val topadsDashboardDeposits: TopadsDashboardDeposits = TopadsDashboardDeposits()
)
data class TopadsDashboardDeposits(
        @SerializedName("data")
        val `data`: DepositAmount = DepositAmount()
)

data class DepositAmount(
        @SerializedName("amount")
        val amount: Int = 0,
        @SerializedName("amount_fmt")
        val amountFmt: String = "",
        @SerializedName("amount_html")
        val amountHtml: String = ""
)
