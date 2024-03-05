package com.tokopedia.play.broadcaster.ui.model.stats

/**
 * Created by Jonathan Darwin on 05 March 2024
 */
sealed interface LiveStatsCardModel {

    val liveStats: LiveStatsUiModel

    data class Clickable(
        override val liveStats: LiveStatsUiModel,
        val clickableIcon: Int,
        val onClick: () -> Unit,
    ) : LiveStatsCardModel

    data class NotClickable(
        override val liveStats: LiveStatsUiModel,
    ) : LiveStatsCardModel
}
