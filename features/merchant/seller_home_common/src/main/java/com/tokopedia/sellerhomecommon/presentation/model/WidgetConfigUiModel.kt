package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 16/07/22.
 */

data class WidgetConfigUiModel(
    val title: String = String.EMPTY,
    val appLink: String = String.EMPTY,
    val ctaText: String = String.EMPTY,
    val maxData: Int = Int.ZERO,
    val maxDisplay: Int = Int.ZERO,
    val emptyStateUiModel: WidgetEmptyStateUiModel = WidgetEmptyStateUiModel()
)