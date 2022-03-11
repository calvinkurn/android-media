package com.tokopedia.play.broadcaster.ui.action

import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryAction {
    /** Report Page */
    object ClickCloseReportPage: PlayBroadcastSummaryAction()
    object ClickViewLeaderboard: PlayBroadcastSummaryAction()
    object ClickPostVideo: PlayBroadcastSummaryAction()

    /** Post Video Page */
    object ClickBackToReportPage: PlayBroadcastSummaryAction()
    object ClickEditCover: PlayBroadcastSummaryAction()
    data class ToggleTag(val tagUiModel: PlayTagUiModel): PlayBroadcastSummaryAction()
    object ClickPostVideoNow: PlayBroadcastSummaryAction()
}