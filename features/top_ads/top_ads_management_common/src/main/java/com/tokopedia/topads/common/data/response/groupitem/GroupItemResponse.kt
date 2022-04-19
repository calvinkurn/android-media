package com.tokopedia.topads.common.data.response.groupitem

import com.google.gson.annotations.SerializedName

data class GroupItemResponse(

        @field:SerializedName("GetTopadsDashboardGroups")
        val getTopadsDashboardGroups: GetTopadsDashboardGroups = GetTopadsDashboardGroups()
) {

    data class GetTopadsDashboardGroups(

            @field:SerializedName("separate_statistic")
            val separateStatistic: String = "",

            @field:SerializedName("data")
            val data: List<DataItem> = listOf(),

            @field:SerializedName("meta")
            val meta: Meta = Meta()

    ) {
        data class Meta(

                @field:SerializedName("page")
                val page: Page = Page()
        ) {
            data class Page(

                    @field:SerializedName("per_page")
                    val perPage: Int = 0,

                    @field:SerializedName("current")
                    val current: Int = 0,

                    @field:SerializedName("total")
                    val total: Int = 0

            )
        }
    }
}