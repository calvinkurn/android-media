package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

data class DashGroupListResponse(

        @field:SerializedName("GetTopadsDashboardGroupsV3")
        val getTopadsDashboardGroups: GetTopadsDashboardGroups = GetTopadsDashboardGroups()
)

data class GetTopadsDashboardGroups(

        @field:SerializedName("data")
        val data: List<GroupListDataItem> = listOf(),

        @field:SerializedName("errors")
        val errors: List<ErrorsItem> = listOf(),

        @field:SerializedName("meta")
        val meta: Meta = Meta()
)

data class GroupListDataItem(

        @field:SerializedName("group_type")
        val type: String = "",

        @field:SerializedName("total_item")
        val totalItem: Int = 0,

        @field:SerializedName("total_keyword")
        val totalKeyword: Int = 0,

        @field:SerializedName("group_status_desc")
        val groupStatusDesc: String = "",

        @field:SerializedName("group_id")
        val groupId: String = "0",

        @field:SerializedName("group_name")
        val groupName: String = "",

        @field:SerializedName("strategies")
        val strategies: List<String> = listOf(),

        var isSelected: Boolean = false
)

data class Meta(

        @field:SerializedName("page")
        val page: Page = Page()
)

data class Page(

        @field:SerializedName("per_page")
        val perPage: Int = 0,

        @field:SerializedName("current")
        val current: Int = 0,

        @field:SerializedName("total")
        val total: Int = 0

)



