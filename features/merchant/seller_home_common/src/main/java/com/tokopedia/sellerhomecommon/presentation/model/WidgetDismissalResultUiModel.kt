package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 05/09/22.
 */

data class WidgetDismissalResultUiModel(
    val widgetId: String = String.EMPTY,
    val itemIds: List<String> = emptyList(),
    val isError: Boolean = false,
    val errorMsg: String = String.EMPTY,
    val dismissToken: String = String.EMPTY,
    val state: String = String.EMPTY
)