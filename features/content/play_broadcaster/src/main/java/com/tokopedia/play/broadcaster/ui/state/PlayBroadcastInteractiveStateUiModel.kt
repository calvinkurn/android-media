package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel

/**
 * Created by kenny.hadisaputra on 13/04/22
 */
data class PlayBroadcastInteractiveStateUiModel(
    val interactive: InteractiveUiModel,
    val isPlaying: Boolean,
) {
    companion object {
        val Empty: PlayBroadcastInteractiveStateUiModel
            get() = PlayBroadcastInteractiveStateUiModel(
                interactive = InteractiveUiModel.Unknown,
                isPlaying = false,
            )
    }
}