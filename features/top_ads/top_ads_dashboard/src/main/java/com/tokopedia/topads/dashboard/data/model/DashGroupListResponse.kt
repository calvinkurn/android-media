package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class DashGroupListResponse(

        @field:SerializedName("GetTopadsDashboardGroups")
        val getTopadsDashboardGroups: GetTopadsDashboardGroups = GetTopadsDashboardGroups()
)

data class GetTopadsDashboardGroups(

        @field:SerializedName("data")
        val data: List<GroupListDataItem> = listOf()
)

data class GroupListDataItem(

        @field:SerializedName("total_item")
        val totalItem: Int = 0,

        @field:SerializedName("total_keyword")
        val totalKeyword: Int = 0,

        @field:SerializedName("group_status_desc")
        val groupStatusDesc: String = "",

        @field:SerializedName("group_id")
        val groupId: Int = 0,

        @field:SerializedName("group_name")
        val groupName: String = ""
)



