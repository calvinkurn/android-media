package com.tokopedia.play.broadcaster.shorts.ui.model

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
data class PlayShortsMediaUiModel(
    val mediaUri: String,
) {
    companion object {
        val Empty: PlayShortsMediaUiModel
            get() = PlayShortsMediaUiModel(
                mediaUri = ""
            )
    }
}
