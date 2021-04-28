package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 11/08/20
 */

data class TickerItemUiModel(
        val redirectUrl: String = "",
        val color: String? = "",
        val id: String = "",
        val message: String = "",
        val title: String = "",
        val type: Int = 0,
        val isFromCache: Boolean = false
)