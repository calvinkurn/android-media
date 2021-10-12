package com.tokopedia.play.broadcaster.ui.event

/**
 * Created by jegul on 12/10/21
 */
sealed class PlayBroadcastUiEvent {

    data class SetPinnedMessage(val message: String) : PlayBroadcastUiEvent()
}