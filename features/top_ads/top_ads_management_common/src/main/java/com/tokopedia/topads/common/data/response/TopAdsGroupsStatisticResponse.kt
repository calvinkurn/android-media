package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class TopAdsGroupsStatisticResponseResponse(
    @SerializedName("GetTopadsDashboardGroupStatisticsV3")
    val response:GetTopadsDashboardGroupStatisticsV3? = null
)

data class GetTopadsDashboardGroupStatisticsV3(
    @SerializedName("page")
    val page:PageType? = PageType(),
    @SerializedName("data")
    val data:List<DashboardGroupStatistic>? = listOf(),
    @SerializedName("errors")
    val errors:List<TopAdsDashboardError>? = listOf()
)

data class DashboardGroupStatistic(
    @SerializedName("group_id")
    val groupId:String = "",
    @SerializedName("stat_total_impression")
    val statTotalImpression:String = "",
    @SerializedName("stat_total_click")
    val statTotatClick:String = "",
    @SerializedName("stat_total_conversion")
    val statTotalConversion:String = ""
)
