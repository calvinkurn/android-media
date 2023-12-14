package com.tokopedia.play.broadcaster.shorts.ui.model

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
data class PlayShortsInterspersingConfigUiModel(
    val isInterspersingAllowed: Boolean,
    val isInterspersing: Boolean,
) {
    companion object {
        val Empty: PlayShortsInterspersingConfigUiModel
            get() = PlayShortsInterspersingConfigUiModel(
                isInterspersingAllowed = false,
                isInterspersing = false,
            )
    }
}
