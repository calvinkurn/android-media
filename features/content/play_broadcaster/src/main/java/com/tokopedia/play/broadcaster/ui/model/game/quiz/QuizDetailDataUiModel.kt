package com.tokopedia.play.broadcaster.ui.model.game.quiz

data class QuizDetailDataUiModel(
    val question: String = "",
    val reward:String = "",
    val countDownEnd: Int = 0,
    val choices: List<Choice> = emptyList(),
    val interactiveId: String = "",
) {
    data class Choice(
        val id: String = "",
        val text: String = "",
        val isCorrectAnswer: Boolean = false,
        val participantCount: Int = 0,
    )
}