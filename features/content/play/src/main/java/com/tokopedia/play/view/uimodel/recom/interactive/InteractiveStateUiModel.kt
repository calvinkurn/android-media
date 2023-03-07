package com.tokopedia.play.view.uimodel.recom.interactive

import com.tokopedia.play_common.model.dto.interactive.GameUiModel

/**
 * Created by kenny.hadisaputra on 13/04/22
 */
data class InteractiveStateUiModel(
    val game: GameUiModel,
    val isPlaying: Boolean,
) {
    companion object {
        val Empty: InteractiveStateUiModel
            get() = InteractiveStateUiModel(
                game = GameUiModel.Unknown,
                isPlaying = false,
            )
    }
}
