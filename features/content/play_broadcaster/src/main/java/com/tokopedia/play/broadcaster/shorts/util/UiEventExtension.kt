package com.tokopedia.play.broadcaster.shorts.util

import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
fun MutableStateFlow<PlayShortsUiEvent>.oneTimeUpdate(block: (PlayShortsUiEvent) -> PlayShortsUiEvent) {
    update {
        block(it)
    }
    update {
        PlayShortsUiEvent.Empty
    }
}
