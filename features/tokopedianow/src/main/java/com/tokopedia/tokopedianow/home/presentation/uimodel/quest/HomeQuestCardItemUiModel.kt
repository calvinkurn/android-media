package com.tokopedia.tokopedianow.home.presentation.uimodel.quest

data class HomeQuestCardItemUiModel(
    val id: String,
    val channelId: String,
    val title: String,
    val description: String,
    val isLockedShown: Boolean,
    val showStartBtn: Boolean,
    val isLoading: Boolean,
    val currentProgress: Float,
    val totalProgress: Float,
    val isIdle: Boolean
) {

    fun isFinished(): Boolean {
        return currentProgress == totalProgress
    }
}
