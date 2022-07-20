package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 16/07/22.
 */

data class UnificationTabUiModel(
    var data: BaseDataUiModel? = null,
    val title: String = String.EMPTY,
    val isNew: Boolean = false,
    val widgetType: String = "",
    val dataKey: String = "",
    val metricParam: String = "",
    var isSelected: Boolean = false,
    val config: WidgetConfigUiModel = WidgetConfigUiModel(),
)