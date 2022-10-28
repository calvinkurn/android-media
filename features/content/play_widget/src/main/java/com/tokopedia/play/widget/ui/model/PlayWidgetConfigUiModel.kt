package com.tokopedia.play.widget.ui.model

/**
 * Created by mzennis on 05/10/20
 */
data class PlayWidgetConfigUiModel(
    val autoRefresh: Boolean,
    val autoRefreshTimer: Long,
    val autoPlay: Boolean,
    val autoPlayAmount: Int, // maximum card with auto play
    val maxAutoPlayCellularDuration: Int, // maximum video duration, only used for non-wifi user
    val maxAutoPlayWifiDuration: Int, // maximum video duration,
    val businessWidgetPosition: Int,
) {
    companion object {
        val Empty: PlayWidgetConfigUiModel
            get() = PlayWidgetConfigUiModel(
                autoRefresh = false,
                autoRefreshTimer = 0,
                autoPlay = false,
                autoPlayAmount = 0,
                maxAutoPlayCellularDuration = 0,
                maxAutoPlayWifiDuration = 0,
                businessWidgetPosition = 0,
            )
    }
}