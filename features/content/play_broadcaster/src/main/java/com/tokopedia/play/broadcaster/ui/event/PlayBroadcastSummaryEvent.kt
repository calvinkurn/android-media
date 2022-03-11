package com.tokopedia.play.broadcaster.ui.event

import androidx.annotation.StringRes
import com.tokopedia.play_common.model.result.NetworkResult


/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryEvent {
    data class ShowInfo(val uiString: UiString): PlayBroadcastSummaryEvent()

    object CloseReportPage: PlayBroadcastSummaryEvent()
    object OpenPostVideoPage: PlayBroadcastSummaryEvent()
    object OpenLeaderboardBottomSheet: PlayBroadcastSummaryEvent()

    object BackToReportPage: PlayBroadcastSummaryEvent()
    object OpenSelectCoverBottomSheet: PlayBroadcastSummaryEvent()
    data class PostVideo(val networkResult: NetworkResult<Boolean>): PlayBroadcastSummaryEvent()
}

sealed class UiString {
    data class Resource(@StringRes val resource: Int) : UiString()
    data class Text(val text: String) : UiString()
}