package com.tokopedia.play.broadcaster.domain.model.interactive.quiz

import com.google.gson.annotations.SerializedName

data class GetInteractiveQuizDetailResponse(
  @SerializedName("playInteractiveGetQuizDetails")
  val playInteractiveQuizDetail: InteractiveQuizDetail = InteractiveQuizDetail()
) {

  data class InteractiveQuizDetail(
    @SerializedName("question")
    val question: String = "",
    @SerializedName("reward")
    val reward: String = "",
    @SerializedName("countdownEnd")
    val countdownEnd: Int = 0,
    @SerializedName("choices")
    val choices: List<Choice> = emptyList(),
  ) {
    data class Choice(
      @SerializedName("id")
      val id: String = "",
      @SerializedName("text")
      val text: String = "",
      @SerializedName("isCorrectAnswer")
      val isCorrectAnswer: Boolean = false,
      @SerializedName("participantCount")
      val participantCount: Int = 0
    )
  }
}