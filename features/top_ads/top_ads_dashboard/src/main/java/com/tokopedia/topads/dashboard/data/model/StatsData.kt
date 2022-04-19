package com.tokopedia.topads.dashboard.data.model


import com.google.gson.annotations.SerializedName

data class StatsData(
    @SerializedName("topadsDashboardStatisticsV2")
    val topadsDashboardStatistics: TopadsDashboardStatistics = TopadsDashboardStatistics()
)

data class TopadsDashboardStatistics(
        @SerializedName("data")
        val `data`: DataStatistic = DataStatistic()
)