package com.tokopedia.sellerpersona.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class OptionUiModel(
    val value: String = String.EMPTY,
    val title: String = String.EMPTY,
    var isSelected: Boolean = false
)