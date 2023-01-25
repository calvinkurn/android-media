package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

data class AdGroupsParams(
    @SerializedName("separate_statistic")
  val separateStatistic:String? = "",
    @SerializedName("shop_id")
    val shopId:String = "",
    @SerializedName("start_date")
    val startDate:String = "",
    @SerializedName("end_date")
    val endDate:String = "",
    @SerializedName("keyword")
    val keyword:String = "",
    @SerializedName("sort")
    val sort:String = "",
    @SerializedName("goal_id")
    val goalId:String = "",
    @SerializedName("group_type")
    val groupType:Int = 0,
    @SerializedName("page")
    val page:Int = 1
)

data class AdGroupStatsParam(
    @SerializedName("separate_statistic")
    val separateStatistic:String = "",
    @SerializedName("shop_id")
    val shopId:String = "",
    @SerializedName("start_date")
    val startDate:String = "",
    @SerializedName("end_date")
    val endDate:String = "",
    @SerializedName("keyword")
    val keyword:String = "",
    @SerializedName("sort")
    val sort:String = "",
    @SerializedName("goal_id")
    val goalId:String = "",
    @SerializedName("page")
    val page:Int = 1,
    @SerializedName("group_ids")
    val groupIds:String = ""
)
