package com.tokopedia.play.broadcaster.ui.model.stats

import com.tokopedia.iconunify.IconUnify

/**
 * Created by Jonathan Darwin on 29 February 2024
 */

sealed interface LiveStatsUiModel {
    val icon: Int
    val text: String

    data class Viewer(
        override val text: String,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.USER
    }

    data class TotalViewer(
        override val text: String,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.VISIBILITY
    }

    data class EstimatedIncome(
        override val text: String,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.SALDO
    }

    data class Like(
        override val text: String,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.THUMB
    }

    data class Duration(
        override val text: String,
    ) : LiveStatsUiModel {
        override val icon: Int = IconUnify.CLOCK
    }
}
