package com.tokopedia.play.view.uimodel.recom.interactive

import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel

/**
 * Created by kenny.hadisaputra on 13/04/22
 */
data class InteractiveStateUiModel(
    val interactive: InteractiveUiModel,
    val isPlaying: Boolean,
) {
    companion object {
        val Empty: InteractiveStateUiModel
            get() = InteractiveStateUiModel(
                interactive = InteractiveUiModel.Unknown,
                isPlaying = false,
            )
    }
}