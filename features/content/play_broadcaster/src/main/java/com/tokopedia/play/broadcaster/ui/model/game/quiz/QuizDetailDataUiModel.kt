package com.tokopedia.play.broadcaster.ui.model.game.quiz

data class QuizDetailDataUiModel(
    val question: String,
    val reward:String,
    val countDownEnd: Int,
    val choices: List<Choice> = listOf(),
) {
    data class Choice(
        val id: String,
        val text: String,
        val isCorrectAnswer: Boolean,
        val participantCount: Int,
    )
}