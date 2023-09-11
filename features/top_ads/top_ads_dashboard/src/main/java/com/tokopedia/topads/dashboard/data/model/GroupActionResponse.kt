package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.model.ErrorsItem

data class GroupActionResponse(

        @field:SerializedName("topAdsEditGroupBulk")
        val topAdsEditGroupBulk: TopAdsEditGroupBulk? = null
) {
    data class TopAdsEditGroupBulk(

            @field:SerializedName("data")
            val data: Data = Data(),

            @field:SerializedName("errors")
            val errors: List<ErrorsItem> = listOf()
    ) {
        data class Data(

                @field:SerializedName("action")
                val action: String = "",

                @field:SerializedName("groups")
                val groups: List<GroupsItem> = listOf(),

                @field:SerializedName("shopID")
                val shopID: String = ""
        ) {
            data class GroupsItem(

                    @field:SerializedName("statusDesc")
                    val statusDesc: String = "",

                    @field:SerializedName("groupID")
                    val groupID: String = "",

                    @field:SerializedName("status")
                    val status: String = ""
            )
        }
    }

}
