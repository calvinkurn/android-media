package com.dompetia.sellerhomecommon.presentation.view.viewholder

import com.dompetia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.dompetia.sellerhomecommon.presentation.model.TooltipUiModel

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface BaseViewHolderListener {

    fun onTooltipClicked(tooltip: TooltipUiModel)

    fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>)

    fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>)
}