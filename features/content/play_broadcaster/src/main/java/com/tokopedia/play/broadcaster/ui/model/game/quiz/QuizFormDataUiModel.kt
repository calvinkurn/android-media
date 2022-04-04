package com.tokopedia.play.broadcaster.ui.model.game.quiz

/**
 * Created By : Jonathan Darwin on March 31, 2022
 */
data class QuizFormDataUiModel(
    val title: String = "",
    val gift: String = "",
    val options: List<Option> = emptyList(),
    val duration: Long = 0,
) {
    data class Option(
        val order: Int = 0,
        val text: String = "",
        val maxLength: Int = 0,
        val isMandatory: Boolean = false,
        val isSelected: Boolean = false,
        val isEditable: Boolean = true,
    ) {
        fun getTextChoice() = (INITIAL_CHOICE + order).toString()

        companion object {
            private const val INITIAL_CHOICE = 'A'
        }
    }

    fun isFormValid(): Boolean {
        return title.isNotEmpty() && isOptionsValid()
    }

    private fun isOptionsValid(): Boolean {
        return true
        /** TODO: gonna uncomment this */
//        val isMandatoryFilled = options.none { it.isMandatory && it.text.isEmpty() }
//        val isSelected = options.find { it.isSelected } != null
//
//        return isMandatoryFilled && isSelected
    }
}