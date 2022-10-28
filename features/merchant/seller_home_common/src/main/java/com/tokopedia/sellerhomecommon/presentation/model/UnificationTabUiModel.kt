package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by @ilhamsuaib on 16/07/22.
 */

data class UnificationTabUiModel(
    var data: BaseDataUiModel? = null,
    val title: String = String.EMPTY,
    val isNew: Boolean = false,
    val isUnauthorized: Boolean = false,
    val itemCount: Int = Int.ZERO,
    val tooltip: String = String.EMPTY,
    val widgetType: String = String.EMPTY,
    val dataKey: String = String.EMPTY,
    val metricParam: String = String.EMPTY,
    var isSelected: Boolean = false,
    val config: WidgetConfigUiModel = WidgetConfigUiModel(),
    var isVisited: Boolean = false,
    var impressHolder: ImpressHolder = ImpressHolder()
)