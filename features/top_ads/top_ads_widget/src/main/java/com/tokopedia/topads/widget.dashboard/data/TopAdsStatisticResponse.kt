package com.tokopedia.topads.widget.dashboard.data


import com.google.gson.annotations.SerializedName

data class TopAdsStatisticResponse(
        @SerializedName("data")
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("topAdsGetAutoAds")
            val topAdsGetAutoAds: TopAdsGetAutoAds = TopAdsGetAutoAds(),
            @SerializedName("topAdsGetShopInfo")
            val topAdsGetShopInfo: TopAdsGetShopInfo = TopAdsGetShopInfo(),
            @SerializedName("topadsDashboardStatistics")
            val topadsDashboardStatistics: TopadsDashboardStatistics = TopadsDashboardStatistics()
    ) {
        data class TopAdsGetAutoAds(
                @SerializedName("data")
                val `data`: Data = Data()
        ) {
            data class Data(
                    @SerializedName("daily_budget")
                    val dailyBudget: Int = 0,
                    @SerializedName("daily_usage")
                    val dailyUsage: Int = 0,
                    @SerializedName("info")
                    val info: Info = Info(),
                    @SerializedName("shop_id")
                    val shopId: Int = 0,
                    @SerializedName("status")
                    val status: Int = 0,
                    @SerializedName("status_desc")
                    val statusDesc: String = ""
            ) {
                data class Info(
                        @SerializedName("message")
                        val message: String = "",
                        @SerializedName("reason")
                        val reason: String = ""
                )
            }
        }

        data class TopAdsGetShopInfo(
                @SerializedName("data")
                val `data`: Data = Data()
        ) {
            data class Data(
                    @SerializedName("category")
                    val category: Int = 0,
                    @SerializedName("category_desc")
                    val categoryDesc: String = ""
            )
        }

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