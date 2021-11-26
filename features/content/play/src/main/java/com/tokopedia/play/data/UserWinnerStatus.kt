package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on October 04, 2021
 */
data class UserWinnerStatus(
    @SerializedName("channel_id")
    val channelId: Long = 0L,

    @SerializedName("interactive_id")
    val interactiveId: Long = 0L,

    @SerializedName("user_id")
    val userId: Long = 0L,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("winner_title")
    val winnerTitle: String = "",

    @SerializedName("winner_text")
    val winnerText: String = "",

    @SerializedName("loser_title")
    val loserTitle: String = "",

    @SerializedName("loser_text")
    val loserText: String = ""
)