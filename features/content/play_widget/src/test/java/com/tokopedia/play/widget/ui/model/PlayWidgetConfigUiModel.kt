package com.tokopedia.play.widget.ui.model


/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidgetConfigUiModel(
        val autoRefresh: Boolean,
        val autoRefreshTimer: Long,
        val autoPlay: Boolean,
        val autoPlayAmount: Long,
        val maxAutoPlayCard: Long
)