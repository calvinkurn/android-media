package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 19/05/20
 */

data class TooltipUiModel(
        val title: String,
        val content: String,
        val shouldShow: Boolean,
        val list: List<TooltipListItemUiModel>
)