package com.tokopedia.play.broadcaster.shorts.ui.model

/**
 * Created By : Jonathan Darwin on January 03, 2024
 */
sealed interface ShortsCoverState {

    object Unknown : ShortsCoverState

    object Loading : ShortsCoverState

    data class Success(
        val uri: String
    ) : ShortsCoverState

    object Error : ShortsCoverState
}
