package com.tokopedia.play_common.domain.usecase.interactive

import com.google.gson.annotations.SerializedName
import com.tokopedia.play_common.domain.model.interactive.ChannelQuiz
import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse

/**
 * @author by astidhiyaa on 06/04/22
 */
data class GetLeaderboardSlotResponse (
    @SerializedName("slots")
    val data: List<SlotData> = emptyList()
){
    data class SlotData(
        //Giveaway
        @SerializedName("title")
        val title: String = "",

        //Quiz
        @SerializedName("question")
        val question: String = "",

        @SerializedName("reward")
        val reward: String = "",

        @SerializedName("user_choice")
        val userChoice: String = "",

        @SerializedName("choices")
        val choices: List<ChannelQuiz.Choices> = emptyList(),

        //Must-have
        @SerializedName("winners")
        val winner: List<GetInteractiveLeaderboardResponse.Winner> = emptyList(),

        @SerializedName("otherParticipantCountText")
        val otherParticipantCountText: String = "",

        @SerializedName("otherParticipantCount")
        val otherParticipantCount: Int = 0,

        @SerializedName("emptyLeaderboardCopyText")
        val emptyLeaderboardCopyText: String = "",
    )
}