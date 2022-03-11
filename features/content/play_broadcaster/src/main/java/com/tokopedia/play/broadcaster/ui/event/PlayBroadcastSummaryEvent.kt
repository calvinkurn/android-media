package com.tokopedia.play.broadcaster.ui.event


/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryEvent {
    object CloseReportPage: PlayBroadcastSummaryEvent()
    object OpenPostVideoPage: PlayBroadcastSummaryEvent()
    object OpenLeaderboardBottomSheet: PlayBroadcastSummaryEvent()
}