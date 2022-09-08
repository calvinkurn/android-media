package com.tokopedia.play.broadcaster.ui.model.interactive

import com.tokopedia.play.broadcaster.ui.model.game.GameType

/**
 * Created by kenny.hadisaputra on 20/04/22
 */
data class InteractiveSetupUiModel(
    val type: GameType,
    val isSubmitting: Boolean,
) {
    companion object {
        val Empty: InteractiveSetupUiModel
            get() = InteractiveSetupUiModel(
                type = GameType.Unknown,
                isSubmitting = false,
            )
    }
}