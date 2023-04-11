package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play_common.model.dto.interactive.GameUiModel

/**
 * Created by kenny.hadisaputra on 13/04/22
 */
data class PlayBroadcastInteractiveStateUiModel(
    val game: GameUiModel,
    val isPlaying: Boolean,
) {
    companion object {
        val Empty: PlayBroadcastInteractiveStateUiModel
            get() = PlayBroadcastInteractiveStateUiModel(
                game = GameUiModel.Unknown,
                isPlaying = false,
            )
    }
}
