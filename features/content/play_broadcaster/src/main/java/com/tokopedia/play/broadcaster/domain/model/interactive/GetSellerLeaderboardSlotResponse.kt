package com.tokopedia.play.broadcaster.domain.model.interactive

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizChoiceDetailResponse

/**
 * @author by andriyan on 18/05/22
 */
data class GetSellerLeaderboardSlotResponse (
    @SerializedName("playInteractiveSellerGetLeaderboardWithSlot")
    val data: Data = Data()
){
    data class Data(
        @SerializedName("slots")
        val slots: List<SlotData> = emptyList()
    )

    data class SlotData(
        //Giveaway
        @SerializedName("title")
        val title: String = "",

        //Quiz
        @SerializedName("question")
        val question: String = "",

        @SerializedName("reward")
        val reward: String = "",

        @SerializedName("choices")
        val choices: List<GetInteractiveQuizChoiceDetailResponse.Choice> = emptyList(),

        //Must-have
        @SerializedName("interactiveID")
        val interactiveId: String = "",

        @SerializedName("winners")
        val winner: List<Winner> = emptyList(),

        @SerializedName("otherParticipantCountText")
        val otherParticipantCountText: String = "",

        @SerializedName("otherParticipantCount")
        val otherParticipantCount: Int = 0,

        @SerializedName("__typename")
        val type: String = "",
    )

    data class Winner(
        @SerializedName("userID")
        val userID: String = "",

        @SerializedName("userName")
        val userName: String = "",

        @SerializedName("imageUrl")
        val imageUrl: String = ""
    )
}