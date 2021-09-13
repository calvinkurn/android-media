package com.tokopedia.play_common.domain.model.interactive

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 02/07/21
 */
data class GetInteractiveLeaderboardResponse(
        @SerializedName("playInteractiveGetSummaryLeaderboard")
        val data: LeaderboardData = LeaderboardData()
) {

    data class LeaderboardData(
            @SerializedName("data")
            val data: List<Data> = emptyList(),

            @SerializedName("config")
            val config: Config = Config(),

            @SerializedName("summary")
            val summary: Summary = Summary()
    )

    data class Data(
            @SerializedName("title")
            val title: String = "",

            @SerializedName("winner")
            val winner: List<Winner> = emptyList(),

            @SerializedName("otherParticipantCountText")
            val otherParticipantCountText: String = "",

            @SerializedName("otherParticipantCount")
            val otherParticipantCount: Int = 0,
    )

    data class Winner(
            @SerializedName("userID")
            val userID: Long = 0L,

            @SerializedName("userName")
            val userName: String = "",

            @SerializedName("imageURL")
            val imageUrl: String = ""
    )

    data class Config(
            @SerializedName("sellerMessage")
            val sellerMessage: String = "",

            @SerializedName("winnerMessage")
            val winnerMessage: String = "",

            @SerializedName("winnerDetail")
            val winnerDetail: String = "",

            @SerializedName("loserMessage")
            val loserMessage: String = "",

            @SerializedName("loserDetail")
            val loserDetail: String = "",

            @SerializedName("topchatMessage")
            val topChatMessage: String = "",
    )

    data class Summary(
            @SerializedName("totalParticipant")
            val totalParticipant: Int = 0
    )
}