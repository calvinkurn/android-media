package com.tokopedia.chatbot.domain.pojo.ratinglist


import com.google.gson.annotations.SerializedName

data class ChipGetChatRatingListResponse(
    @SerializedName("chipGetChatRatingListV5")
    val chipGetChatRatingList: ChipGetChatRatingList?
) {
    data class ChipGetChatRatingList(
            @SerializedName("data")
        val ratingListData: RatingListData?,
            @SerializedName("messageError")
        val messageError: List<String>?,
            @SerializedName("serverProcessTime")
        val serverProcessTime: String?,
            @SerializedName("status")
        val status: String?
    ) {
        data class RatingListData(
            @SerializedName("isSuccess")
            val isSuccess: Int?,
            @SerializedName("list")
            val list: List<RatingList>?
        ) {
            data class RatingList(
                @SerializedName("caseChatID")
                val caseChatID: String?,
                @SerializedName("isSubmitted")
                val isSubmitted: Boolean?,
                @SerializedName("value")
                val value: String?,
                @SerializedName("attachmentType")
                val attachmentType: Int?
            )
        }
    }
}