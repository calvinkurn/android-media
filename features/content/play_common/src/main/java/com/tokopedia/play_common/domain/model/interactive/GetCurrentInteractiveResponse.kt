package com.tokopedia.play_common.domain.model.interactive

import com.google.gson.annotations.SerializedName

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
data class GetCurrentInteractiveResponse(
    @SerializedName("playInteractiveGetCurrentInteractive")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("interactive")
        val giveaway: GiveawayResponse = GiveawayResponse(),

        @SerializedName("quiz")
        val quiz: QuizResponse = QuizResponse(),

        @SerializedName("meta")
        val meta: Meta = Meta(),
    )

    data class Meta(
        @SerializedName("active")
        val active: String = "",

        @SerializedName("waiting_duration")
        val waitingDuration: Int = 0,
    )
}

data class GiveawayResponse(
    @SerializedName("channel_id")
    val channelID: Long = 0,

    @SerializedName("interactive_id")
    val interactiveID: String = "0",

    @SerializedName("interactive_type")
    val interactiveType: Int = -1,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("countdown_start")
    val countdownStart: Int = 0,

    @SerializedName("countdown_end")
    val countdownEnd: Int = 0,

    @SerializedName("countdown_end_delay")
    val countdownEndDelay: Int = 0,

    @SerializedName("waiting_duration")
    val waitingDuration: Int = 0,
)

data class QuizResponse(
    @SerializedName("interactive_id")
    val interactiveID: String = "",

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("question")
    val question: String = "",

    @SerializedName("countdown_end")
    val countdownEnd: Int = 0,

    @SerializedName("choices")
    val choices: List<Choice> = emptyList(),

    @SerializedName("user_choice")
    val userChoice: String = "",

    @SerializedName("waiting_duration")
    val waitingDuration: Long = 0L,
) {

    data class Choice(
        @SerializedName("id")
        val id: String = "0",

        @SerializedName("text")
        val text: String = "",

        @SerializedName("isCorrect")
        val isCorrect: Boolean? = null,
    )
}
