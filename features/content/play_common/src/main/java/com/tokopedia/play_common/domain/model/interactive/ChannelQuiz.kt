package com.tokopedia.play_common.domain.model.interactive

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 30/03/22
 */
data class ChannelQuiz(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("channel_id")
        val channelID: String = "",

        @SerializedName("interactive_id")
        val interactiveID: String = "",

        @SerializedName("interactive_type")
        val interactiveType: Int = -1,

        @SerializedName("status")
        val status: Int = 0,

        @SerializedName("countdown_end")
        val countdownEnd: Int = 0,

        @SerializedName("question")
        val question: String = "",

        @SerializedName("prize")
        val prize: String = "",

        @SerializedName("choices")
        val choices: Choices = Choices(),

        @SerializedName("user_choice")
        val userChoice: Int = 0,
    )

    data class Choices(
        @SerializedName("id")
        val choicesID: String = "",

        @SerializedName("text")
        val choicesText: String = "",
    )
}