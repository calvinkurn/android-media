package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class TopAdsGroupsResponse(
    @SerializedName("GetTopadsDashboardGroupsV3")
    val response:GetTopAdsDashboardGroupsV3? = null
)

data class GetTopAdsDashboardGroupsV3(
    @SerializedName("page")
    val page:PageType? = PageType(),
    @SerializedName("data")
    val data:List<DashboardGroupDataType>? = listOf(),
    @SerializedName("errors")
    val errors:List<TopAdsDashboardError>? = listOf()
)

data class PageType(
    @SerializedName("current")
  val current:Int = 1,
    @SerializedName("per_page")
  val perPage:Int = 0,
    @SerializedName("min")
  val min:Int = 0,
    @SerializedName("max")
  val max:Int = 0,
    @SerializedName("total")
  val total:Int = 0
)

data class DashboardGroupDataType(
    @SerializedName("group_id")
    val groupId:String = "",
    @SerializedName("group_status")
    val groupStatus:Int = 0,
    @SerializedName("group_start_date")
    val groupStartDate:String = "",
    @SerializedName("group_end_date")
    val groupEndDate:String = "",
    @SerializedName("group_name")
    val groupName:String = "",
    @SerializedName("group_type")
    val groupType:String = "",
    @SerializedName("group_bid_setting")
    val groupBidSetting:GroupBidSetting = GroupBidSetting()
)

data class GroupBidSetting(
    @SerializedName("product_browse")
   val productBrowse:Float = 0f,
    @SerializedName("product_search")
   val productSearch:Float = 0f
)

data class TopAdsDashboardError(
    @SerializedName("code")
    val code:String = "",
    @SerializedName("detail")
    val detail:String = "",
    @SerializedName("object")
    val data:TopAdsDashBoardErrorObject? = null,
    @SerializedName("title")
    val title:String = ""
)

data class TopAdsDashBoardErrorObject(
    @SerializedName("type")
    val type:Int = 0,
    @SerializedName("text")
    val text:List<String> = listOf()
)
