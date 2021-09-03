package com.tokopedia.play.view.uimodel

/**
 * Created By : Jonathan Darwin on September 03, 2021
 */
data class PlayUpcomingUiModel(
    val isUpcoming: Boolean
) {
    companion object {
        const val COMING_SOON = "COMING_SOON"
    }
}