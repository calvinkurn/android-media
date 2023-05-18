package com.tokopedia.topads.dashboard.recommendation.data.model.cloud


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*

data class TopAdsBatchGroupInsightResponse(
    @SerializedName("groups")
    val groups: List<Group> = listOf(),
    @SerializedName("error")
    val error: Error = Error(),
) {
    data class Group(
        @SerializedName("data")
        val groupData: GroupData = GroupData()
    ) {
        data class GroupData(
            @SerializedName("existingKeywordsBidRecom")
            val existingKeywordsBidRecom: List<ExistingKeywordsBidRecom> = listOf(),
            @SerializedName("groupID")
            val groupID: String = "",
            @SerializedName("newNegativeKeywordsRecom")
            val newNegativeKeywordsRecom: List<NewNegativeKeywordsRecom> = listOf(),
            @SerializedName("newPositiveKeywordsRecom")
            val newPositiveKeywordsRecom: List<NewPositiveKeywordsRecom> = listOf()
        ) {
            data class ExistingKeywordsBidRecom(
                @SerializedName("currentBid")
                val currentBid: Int = 0,
                @SerializedName("keywordID")
                val keywordID: String = "",
                @SerializedName("keywordStatus")
                val keywordStatus: String = "",
                @SerializedName("keywordTag")
                val keywordTag: String = "",
                @SerializedName("keywordType")
                val keywordType: String = "",
                @SerializedName("predictedImpression")
                val predictedImpression: String = "",
                @SerializedName("suggestionBid")
                val suggestionBid: Int = 0,
                @SerializedName("suggestionBidSource")
                val suggestionBidSource: String = ""
            )

            data class NewNegativeKeywordsRecom(
                @SerializedName("keywordSource")
                val keywordSource: String = "",
                @SerializedName("keywordStatus")
                val keywordStatus: String = "",
                @SerializedName("keywordTag")
                val keywordTag: String = "",
                @SerializedName("keywordType")
                val keywordType: String = "",
                @SerializedName("potentialSavings")
                val potentialSavings: Int = 0,
                @SerializedName("predictedImpression")
                val predictedImpression: String = ""
            )

            data class NewPositiveKeywordsRecom(
                @SerializedName("competition")
                val competition: String = "",
                @SerializedName("keywordSource")
                val keywordSource: String = "",
                @SerializedName("keywordStatus")
                val keywordStatus: String = "",
                @SerializedName("keywordTag")
                val keywordTag: String = "",
                @SerializedName("keywordType")
                val keywordType: String = "",
                @SerializedName("predictedImpression")
                val predictedImpression: String = "",
                @SerializedName("suggestionBid")
                val suggestionBid: Int = 0,
                @SerializedName("totalSearch")
                val totalSearch: String = ""
            )
        }
    }
}
