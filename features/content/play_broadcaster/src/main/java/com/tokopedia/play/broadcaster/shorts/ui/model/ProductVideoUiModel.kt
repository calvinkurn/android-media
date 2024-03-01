package com.tokopedia.play.broadcaster.shorts.ui.model

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
data class ProductVideoUiModel(
    val hasVideo: Boolean,
    val videoUrl: String,
    val coverUrl: String,
) {
    companion object {
        val Empty: ProductVideoUiModel
            get() = ProductVideoUiModel(
                hasVideo = false,
                videoUrl = "",
                coverUrl = "",
            )
    }
}
