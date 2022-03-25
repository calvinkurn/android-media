package com.tokopedia.play.broadcaster.ui.model.tag

/**
 * Created By : Jonathan Darwin on March 25, 2022
 */
data class PlayTagPlaceholderUiModel(
    val size: Size,
) {
    enum class Size {
        SMALL, MEDIUM, LARGE
    }
}