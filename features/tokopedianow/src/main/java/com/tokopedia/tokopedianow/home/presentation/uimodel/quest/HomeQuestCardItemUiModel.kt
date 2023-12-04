package com.tokopedia.tokopedianow.home.presentation.uimodel.quest

data class HomeQuestCardItemUiModel(
    val id: String,
    val title: String,
    val description: String,
    val isLockedShown: Boolean,
    val currentProgress: Float,
    val totalProgress: Float
) {

    fun isFinished(): Boolean {
        return currentProgress == totalProgress
    }
}
