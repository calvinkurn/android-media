package com.tokopedia.play.broadcaster.ui.model.report.live

/**
 * Created by Jonathan Darwin on 05 March 2024
 */
sealed interface LiveStatsCardModel {

    val liveStats: LiveStatsUiModel

    data class Clickable(
        override val liveStats: LiveStatsUiModel,
        val clickableIcon: Int,
        val clickArea: ClickArea,
        val onClick: () -> Unit,
    ) : LiveStatsCardModel {

        enum class ClickArea {
            Full,
            IconOnly,
        }
    }

    data class NotClickable(
        override val liveStats: LiveStatsUiModel,
    ) : LiveStatsCardModel
}
