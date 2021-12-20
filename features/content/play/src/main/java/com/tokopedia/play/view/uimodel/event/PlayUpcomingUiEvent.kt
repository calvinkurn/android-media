package com.tokopedia.play.view.uimodel.event

/**
 * Created By : Jonathan Darwin on November 17, 2021
 */
sealed class PlayUpcomingUiEvent {
    /**
     * General
     */
    data class OpenPageEvent(val applink: String, val params: List<String> = emptyList(), val requestCode: Int? = null) : PlayUpcomingUiEvent()
    data class ShowInfoEvent(val message: UiString) : PlayUpcomingUiEvent()
    data class ShowInfoWithActionEvent(val message: UiString, val action: () -> Unit) : PlayUpcomingUiEvent()

    /**
     * Upcoming
     */
    data class RemindMeEvent(val message: UiString, val isSuccess: Boolean): PlayUpcomingUiEvent()
    object RefreshChannelEvent: PlayUpcomingUiEvent()

    /**
     * Other
     */
    data class CopyToClipboardEvent(val content: String) : PlayUpcomingUiEvent()
}