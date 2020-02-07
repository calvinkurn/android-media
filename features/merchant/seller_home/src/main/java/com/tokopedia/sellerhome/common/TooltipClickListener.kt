package com.tokopedia.sellerhome.common

import com.tokopedia.sellerhome.view.model.TooltipUiModel

interface TooltipClickListener {
    fun onTooltipClicked(tooltip: TooltipUiModel)
}