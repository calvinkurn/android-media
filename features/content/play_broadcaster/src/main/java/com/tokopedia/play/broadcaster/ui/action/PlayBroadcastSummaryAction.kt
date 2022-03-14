package com.tokopedia.play.broadcaster.ui.action

import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryAction {
    /** Report Page */
    data class SetProductSectionList(val productSectionList: List<ProductTagSectionUiModel>): PlayBroadcastSummaryAction()
    object ClickCloseReportPage: PlayBroadcastSummaryAction()
    object ClickViewLeaderboard: PlayBroadcastSummaryAction()
    object ClickPostVideo: PlayBroadcastSummaryAction()

    /** Post Video Page */
    object ClickBackToReportPage: PlayBroadcastSummaryAction()
    object ClickEditCover: PlayBroadcastSummaryAction()
    data class SetCover(val cover: PlayCoverUiModel): PlayBroadcastSummaryAction()
    data class ToggleTag(val tagUiModel: PlayTagUiModel): PlayBroadcastSummaryAction()
    object ClickPostVideoNow: PlayBroadcastSummaryAction()
}