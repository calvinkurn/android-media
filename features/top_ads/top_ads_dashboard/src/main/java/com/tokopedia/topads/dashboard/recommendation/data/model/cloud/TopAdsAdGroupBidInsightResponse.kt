package com.tokopedia.topads.dashboard.recommendation.data.model.cloud


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class TopAdsAdGroupBidInsightResponse(
    @SerializedName("topAdsBatchGetAdGroupBidInsightByGroupID")
    val topAdsBatchGetAdGroupBidInsightByGroupID: TopAdsBatchGetAdGroupBidInsightByGroupID = TopAdsBatchGetAdGroupBidInsightByGroupID()
) {
    data class TopAdsBatchGetAdGroupBidInsightByGroupID(
        @SerializedName("groups")
        val groups: List<Group> = listOf(),
        @SerializedName("error")
        val error: Error = Error(),

        ) {
        data class Group(
            @SerializedName("data")
            val adGroupBidInsightData: AdGroupBidInsightData = AdGroupBidInsightData(),
        ) {
            data class AdGroupBidInsightData(
                @SerializedName("currentBidSettings")
                val currentBidSettings: List<Any> = listOf(),
                @SerializedName("groupID")
                val groupID: String = "",
                @SerializedName("predictedTotalImpression")
                val predictedTotalImpression: String = "",
                @SerializedName("suggestionBidSettings")
                val suggestionBidSettings: List<Any> = listOf(),

                var isSelected : Boolean = false
            )
        }
    }

}
