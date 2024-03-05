package com.tokopedia.play.broadcaster.ui.model.stats

import androidx.annotation.StringRes
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.util.extension.millisToHours
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.play.broadcaster.R

/**
 * Created by Jonathan Darwin on 29 February 2024
 */

sealed interface LiveStatsUiModel {
    val icon: Int
    val label: Int
    val text: String

    data class Viewer(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.USER

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_viewer_label

        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class TotalViewer(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.VISIBILITY

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_total_viewer_label

        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class EstimatedIncome(
        val value: String = "",
        val clickableIcon: Int = IconUnify.CHEVRON_RIGHT,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.SALDO

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_estimated_income_label

        override val text: String
            get() = value.ifEmpty { "Rp0" }
    }

    data class Like(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.THUMB

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_like_label

        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class Duration(
        val timeInMillis: Long = 0,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.CLOCK

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_live_duration_label

        override val text: String
            get() {
                val hour = timeInMillis.millisToHours()
                val minutes = timeInMillis.millisToRemainingMinutes()
                val seconds = timeInMillis.millisToRemainingSeconds()

                return if(hour > 0) {
                    String.format(HHmmss, hour, minutes, seconds)
                }
                else {
                    String.format(mmss, minutes, seconds)
                }
            }

        companion object {
            private const val HHmmss = "%02d:%02d:%02d"
            private const val mmss = "%02d:%02d"
        }
    }

    data class Visit(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.PRODUCT_NEXT

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_visit_label

        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class AddToCart(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.CART

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_add_to_cart_label

        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class TotalSold(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.SHOPPING_BAG

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_total_sold_label

        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class GameParticipant(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.GAME

        @StringRes override val label: Int = R.string.play_broadcaster_live_stats_game_participant_label

        override val text: String
            get() = value.ifEmpty { "0" }
    }
}
