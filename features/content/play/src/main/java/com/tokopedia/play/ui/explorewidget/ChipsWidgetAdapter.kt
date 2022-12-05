package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */

/**
 * Chips Only
 */

class ChipsWidgetAdapter(private val chipsListener: ChipsViewHolder.Listener) : BaseDiffUtilAdapter<ChipWidgetUiModel>() {
    init {
        delegatesManager.addDelegate(ChipsWidgetAdapterDelegate.Chips(chipsListener))
    }
    override fun areItemsTheSame(oldItem: ChipWidgetUiModel, newItem: ChipWidgetUiModel): Boolean {
        return oldItem.group == newItem.group
    }

    override fun areContentsTheSame(oldItem: ChipWidgetUiModel, newItem: ChipWidgetUiModel): Boolean {
        return oldItem == newItem
    }
}
