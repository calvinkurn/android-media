package com.tokopedia.play.broadcaster.domain.model.interactive.quiz


import com.google.gson.annotations.SerializedName

data class GetInteractiveQuizChoiceDetailResponse(
    @SerializedName("playInteractiveGetChoiceDetails")
    val playInteractiveQuizChoiceDetail: InteractiveQuizChoiceDetail = InteractiveQuizChoiceDetail(),
) {

    data class InteractiveQuizChoiceDetail(
        @SerializedName("cursor")
        val cursor: String = "",
        @SerializedName("choice")
        val choice: Choice = Choice(),
        @SerializedName("winners")
        val winners: List<Winner> = emptyList(),
        @SerializedName("participants")
        val participants: List<Participant> = emptyList(),
    )
    data class Choice(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("isCorrectAnswer")
        val isCorrectAnswer: Boolean = false,
        @SerializedName("participantCount")
        val participantCount: Int = 0,
    )

    data class Winner(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("firstName")
        val firstName: String = "",
        @SerializedName("imageURL")
        val imageURL: String = "",
    )

    data class Participant(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("firstName")
        val firstName: String = "",
        @SerializedName("imageURL")
        val imageURL: String = "",
    )
}
