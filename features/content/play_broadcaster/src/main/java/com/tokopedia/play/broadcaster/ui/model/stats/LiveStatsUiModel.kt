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
        override val text: String = "0",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.USER
    }

    data class TotalViewer(
        override val text: String = "0",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.VISIBILITY
    }

    data class EstimatedIncome(
        override val text: String = "Rp0",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.SALDO
    }

    data class Like(
        override val text: String = "0",
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.THUMB
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
