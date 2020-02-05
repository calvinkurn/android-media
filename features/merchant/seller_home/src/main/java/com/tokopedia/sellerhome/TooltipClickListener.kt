package com.tokopedia.sellerhome

import com.tokopedia.sellerhome.view.model.TooltipUiModel

interface TooltipClickListener {
    fun onTooltipClicked(tooltip: TooltipUiModel)
}