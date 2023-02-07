package com.tokopedia.play.ui.explorewidget.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.explorewidget.adapter.delegate.ChipsWidgetAdapterDelegate
import com.tokopedia.play.ui.explorewidget.viewholder.ChipsViewHolder
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.ChipWidgetsUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */

class ChipsWidgetAdapter internal constructor(chipsListener: ChipsViewHolder.Chips.Listener) :
    BaseDiffUtilAdapter<ChipWidgetsUiModel>() {
    init {
        delegatesManager.addDelegate(ChipsWidgetAdapterDelegate.Chips(chipsListener))
        delegatesManager.addDelegate(ChipsWidgetAdapterDelegate.Shimmering())
    }

    override fun areItemsTheSame(
        oldItem: ChipWidgetsUiModel,
        newItem: ChipWidgetsUiModel
    ): Boolean {
        return if (oldItem is ChipWidgetUiModel && newItem is ChipWidgetUiModel)
            oldItem.group == newItem.group else
            oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ChipWidgetsUiModel,
        newItem: ChipWidgetsUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
