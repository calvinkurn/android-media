package com.tokopedia.play.broadcaster.ui.action

/**
 * Created by jegul on 12/10/21
 */
sealed class PlayBroadcastAction {

    object EditPinnedMessage : PlayBroadcastAction()
    data class SetPinnedMessage(val message: String) : PlayBroadcastAction()
    object CancelEditPinnedMessage : PlayBroadcastAction()

    object ExitLive : PlayBroadcastAction()
}