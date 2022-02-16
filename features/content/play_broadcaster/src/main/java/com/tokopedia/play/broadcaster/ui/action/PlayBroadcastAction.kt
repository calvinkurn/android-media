package com.tokopedia.play.broadcaster.ui.action

import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel

/**
 * Created by jegul on 12/10/21
 */
sealed class PlayBroadcastAction {

    object EditPinnedMessage : PlayBroadcastAction()
    data class SetPinnedMessage(val message: String) : PlayBroadcastAction()
    object CancelEditPinnedMessage : PlayBroadcastAction()

    data class SetCover(val cover: PlayCoverUiModel) : PlayBroadcastAction()
    data class SetProduct(val productTagSectionList: List<ProductTagSectionUiModel>) : PlayBroadcastAction()

    object ExitLive : PlayBroadcastAction()
}