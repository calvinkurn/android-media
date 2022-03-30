package com.tokopedia.play.broadcaster.ui.event

import com.tokopedia.play.broadcaster.ui.model.game.GameType

sealed class PlayBroadcastEvent {

    data class ShowError(
        val error: Throwable
    ) : PlayBroadcastEvent()

    data class OpenGameForm(
        val gameType: GameType
    ) : PlayBroadcastEvent()
}