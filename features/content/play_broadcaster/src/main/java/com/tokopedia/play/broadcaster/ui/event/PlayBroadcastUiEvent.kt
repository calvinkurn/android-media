package com.tokopedia.play.broadcaster.ui.event

/**
 * Created by jegul on 12/10/21
 */
sealed class PlayBroadcastUiEvent {

    object EditPinnedMessage : PlayBroadcastUiEvent()
    data class SetPinnedMessage(val message: String) : PlayBroadcastUiEvent()
    object CancelEditPinnedMessage : PlayBroadcastUiEvent()
}