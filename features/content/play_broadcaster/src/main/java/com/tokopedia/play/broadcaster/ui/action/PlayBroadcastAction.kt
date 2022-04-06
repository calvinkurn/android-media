package com.tokopedia.play.broadcaster.ui.action

import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import java.util.*

/**
 * Created by jegul on 12/10/21
 */
sealed interface PlayBroadcastAction {

    object EditPinnedMessage : PlayBroadcastAction
    data class SetPinnedMessage(val message: String) : PlayBroadcastAction
    object CancelEditPinnedMessage : PlayBroadcastAction

    data class SetCover(val cover: PlayCoverUiModel) : PlayBroadcastAction
    data class SetProduct(val productTagSectionList: List<ProductTagSectionUiModel>) : PlayBroadcastAction
    data class SetSchedule(val date: Date) : PlayBroadcastAction
    object DeleteSchedule : PlayBroadcastAction

    object ExitLive : PlayBroadcastAction
}

data class BroadcastStateChanged(val state: PlayBroadcasterState) : PlayBroadcastAction