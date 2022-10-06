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
    val refreshWaitingDuration: Int = REFRESH_WAITING_DURATION,
    val description: String = "",
) {
    companion object {
        const val COMING_SOON = "COMING_SOON"

        const val REFRESH_WAITING_DURATION = 5000
    }
}