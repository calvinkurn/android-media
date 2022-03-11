package com.tokopedia.play.broadcaster.ui.action

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryAction {
    /** Report Page */
    object ClickCloseReportPage: PlayBroadcastSummaryAction()
    object ClickViewLeaderboard: PlayBroadcastSummaryAction()
    object ClickPostVideo: PlayBroadcastSummaryAction()

    /** Post Video Page */
    object ClickPostVideoNow: PlayBroadcastSummaryAction()
}