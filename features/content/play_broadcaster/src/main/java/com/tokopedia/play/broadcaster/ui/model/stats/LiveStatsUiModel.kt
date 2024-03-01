package com.tokopedia.play.broadcaster.ui.model.stats

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.util.extension.millisToHours
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds

/**
 * Created by Jonathan Darwin on 29 February 2024
 */

sealed interface LiveStatsUiModel {
    val icon: Int
    val text: String

    data class Viewer(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.USER
        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class TotalViewer(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.VISIBILITY
        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class EstimatedIncome(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.SALDO
        override val text: String
            get() = value.ifEmpty { "Rp0" }
    }

    data class Like(
        val value: String = "",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.THUMB
        override val text: String
            get() = value.ifEmpty { "0" }
    }

    data class Duration(
        val timeInMillis: Long = 0,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.CLOCK
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
}
