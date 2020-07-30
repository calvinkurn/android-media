package com.tokopedia.sellerhome.view.viewholder

import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.sellerhome.view.model.TooltipUiModel

interface BaseViewHolderListener {

    fun onTooltipClicked(tooltip: TooltipUiModel)

    fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>)

    fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>)
}