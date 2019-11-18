package com.tokopedia.topads.widget.dashboard.data


import com.google.gson.annotations.SerializedName

data class TopAdsDepositResponse(
    @SerializedName("data")
    val `data`: Data = Data()
) {
    data class Data(
        @SerializedName("topadsDashboardDeposits")
        val topadsDashboardDeposits: TopadsDashboardDeposits = TopadsDashboardDeposits()
    ) {
        data class TopadsDashboardDeposits(
            @SerializedName("data")
            val `data`: Data = Data()
        ) {
            data class Data(
                @SerializedName("ad_usage")
                val adUsage: Boolean = false,
                @SerializedName("amount")
                val amount: Int = 0,
                @SerializedName("amount_fmt")
                val amountFmt: String = ""
            )
        }
    }
}