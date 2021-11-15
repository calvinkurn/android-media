package com.tokopedia.play.view.uimodel

/**
 * Created By : Jonathan Darwin on September 03, 2021
 */
data class PlayUpcomingUiModel(
    val title: String = "",
    val isUpcoming: Boolean = false,
    val isReminderSet: Boolean = false,
    val coverUrl: String = "",
    val startTime: String = "",
    val isAlreadyLive: Boolean = false,
) {
    companion object {
        const val COMING_SOON = "COMING_SOON"
    }
}