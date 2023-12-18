package com.tokopedia.product.detail.view.widget.campaign

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class CampaignUiModel(
    val logoUrl: String = "",
    val title: String = "",
    private val endTimeUnix: Int = 0,
    val timerLabel: String = "",
    val stockPercentage: Int = 0,
    val stockLabel: String = "",
    val backgroundColor: List<Color> = listOf()
) {

    companion object {
        private const val MILLIS = 1000L
        private const val THEMATIC_COLOR = 0xFFD72C2C
    }

    val shouldShowLogo
        get() = logoUrl.isNotBlank()

    val endTimeMs
        get() = endTimeUnix * MILLIS
}
