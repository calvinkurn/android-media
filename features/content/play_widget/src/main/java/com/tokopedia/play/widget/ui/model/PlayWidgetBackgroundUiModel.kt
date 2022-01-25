package com.tokopedia.play.widget.ui.model

/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidgetBackgroundUiModel(
    val overlayImageUrl: String,
    val overlayImageAppLink: String,
    val overlayImageWebLink: String,
    val gradientColors: List<String>,
    val backgroundUrl: String
) {
    companion object {
        val Empty: PlayWidgetBackgroundUiModel
            get() = PlayWidgetBackgroundUiModel(
                overlayImageUrl = "",
                overlayImageAppLink = "",
                overlayImageWebLink = "",
                gradientColors = emptyList(),
                backgroundUrl = "",
            )
    }
}