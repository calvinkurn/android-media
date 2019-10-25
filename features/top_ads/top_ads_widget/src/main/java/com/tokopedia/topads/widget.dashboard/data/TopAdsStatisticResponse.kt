package com.tokopedia.topads.widget.dashboard.data


import com.google.gson.annotations.SerializedName

data class TopAdsStatisticResponse(
    @SerializedName("data")
    val `data`: Data = Data()
) {
    data class Data(
        @SerializedName("topadsDashboardStatistics")
        val topadsDashboardStatistics: TopadsDashboardStatistics = TopadsDashboardStatistics()
    ) {
        data class TopadsDashboardStatistics(
            @SerializedName("data")
            val `data`: Data = Data()
        ) {
            data class Data(
                @SerializedName("summary")
                val summary: Summary = Summary()
            ) {
                data class Summary(
                    @SerializedName("ads_all_gross_profit_fmt")
                    val adsAllGrossProfitFmt: String = "",
                    @SerializedName("ads_click_sum_fmt")
                    val adsClickSumFmt: String = "",
                    @SerializedName("ads_cost_sum_fmt")
                    val adsCostSumFmt: String = "",
                    @SerializedName("ads_impression_sum_fmt")
                    val adsImpressionSumFmt: String = ""
                )
            }
        }
    }
}