package com.tokopedia.play.broadcaster.ui.model.interactive

data class QuizDetailUiModel(
    val title: String,
    val question: String,
    val countDownEnd: Int,
    val choices: List<Choice> = listOf(),
) {
    data class Choice(
        val text: String,
        val isCorrectAnswer: Boolean,
        val participantCount: Int,
    )
}