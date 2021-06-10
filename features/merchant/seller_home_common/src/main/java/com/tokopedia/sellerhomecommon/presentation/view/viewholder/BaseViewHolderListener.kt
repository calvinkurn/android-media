package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface BaseViewHolderListener {

    fun onTooltipClicked(tooltip: TooltipUiModel)

    fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>)

    fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>, error: String)
}