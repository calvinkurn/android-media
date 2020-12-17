package com.tokopedia.topads.common.data.response.groupitem

import com.google.gson.annotations.SerializedName

data class GroupStatisticsResponse(

        @field:SerializedName("GetTopadsDashboardGroupStatistics")
        val getTopadsDashboardGroupStatistics: GetTopadsDashboardGroupStatistics = GetTopadsDashboardGroupStatistics()
)

data class GetTopadsDashboardGroupStatistics(

        @field:SerializedName("separate_statistic")
        val separateStatistic: String = "",

        @field:SerializedName("data")
        val data: List<DataItem> = listOf(),

        @field:SerializedName("meta")
        val meta: Meta = Meta()
)

data class Meta(

        @field:SerializedName("page")
        val page: Page = Page()
)

data class Page(

        @field:SerializedName("per_page")
        val perPage: Int = 0,

        @field:SerializedName("current")
        val current: Int = 0
)