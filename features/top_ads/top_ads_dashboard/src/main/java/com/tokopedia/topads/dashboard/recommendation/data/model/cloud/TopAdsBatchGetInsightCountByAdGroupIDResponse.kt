package com.tokopedia.topads.dashboard.recommendation.data.model.cloud


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class TopAdsBatchGetInsightCountByAdGroupIDResponse(
    @SerializedName("topAdsBatchGetInsightCountByAdGroupID")
    val topAdsBatchGetInsightCountByAdGroupID: TopAdsBatchGetInsightCountByAdGroupID
) {
    data class TopAdsBatchGetInsightCountByAdGroupID(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("groups")
        val groups: List<Group>
    ) {
        data class Group(
            @SerializedName("data")
            val groupData: Data
        ) {
            data class Data(
                @SerializedName("count")
                val count: Int
            )
        }
    }
}
