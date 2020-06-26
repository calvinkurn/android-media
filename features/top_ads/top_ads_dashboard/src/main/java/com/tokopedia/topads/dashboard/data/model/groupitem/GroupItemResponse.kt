package com.tokopedia.topads.dashboard.data.model.groupitem

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
                val page: Page? = null
        ) {
            data class Page(

                    @field:SerializedName("per_page")
                    val perPage: Int? = null,

                    @field:SerializedName("current")
                    val current: Int? = null,

                    @field:SerializedName("total")
                    val total: Int? = null,

                    @field:SerializedName("min")
                    val min: Int? = null,

                    @field:SerializedName("max")
                    val max: Int? = null
            )
        }
    }
}