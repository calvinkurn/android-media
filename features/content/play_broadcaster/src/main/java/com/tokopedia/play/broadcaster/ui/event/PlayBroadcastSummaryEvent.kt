package com.tokopedia.play.broadcaster.ui.event

import androidx.annotation.StringRes


/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryEvent {
    data class ShowInfo(val uiString: UiString): PlayBroadcastSummaryEvent()
    object CloseReportPage: PlayBroadcastSummaryEvent()
    object OpenPostVideoPage: PlayBroadcastSummaryEvent()
    object OpenLeaderboardBottomSheet: PlayBroadcastSummaryEvent()
}

sealed class UiString {
    data class Resource(@StringRes val resource: Int) : UiString()
    data class Text(val text: String) : UiString()
}