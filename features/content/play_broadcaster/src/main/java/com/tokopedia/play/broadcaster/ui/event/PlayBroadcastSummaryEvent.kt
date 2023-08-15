package com.tokopedia.play.broadcaster.ui.event

import com.tokopedia.play_common.model.result.NetworkResult


/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryEvent {

    object CloseReportPage: PlayBroadcastSummaryEvent()
    object OpenPostVideoPage: PlayBroadcastSummaryEvent()
    object OpenLeaderboardBottomSheet: PlayBroadcastSummaryEvent()

    object BackToReportPage: PlayBroadcastSummaryEvent()
    object OpenSelectCoverBottomSheet: PlayBroadcastSummaryEvent()
    data class PostVideo(val networkResult: NetworkResult<Boolean>): PlayBroadcastSummaryEvent()
}
