package com.tokopedia.play.broadcaster.ui.model.game.quiz

/**
 * Created By : Jonathan Darwin on March 31, 2022
 */
data class QuizFormDataUiModel(
    val title: String = "",
    val gift: String = "",
    val options: List<Option> = emptyList(),
) {
    data class Option(
        val text: String = "",
        val isMandatory: Boolean = false,
        val isSelected: Boolean = false,
    )
}