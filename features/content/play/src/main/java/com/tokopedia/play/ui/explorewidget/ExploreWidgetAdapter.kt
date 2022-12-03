package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */

/**
 * Chips Only
 */

class ExploreWidgetAdapter : BaseDiffUtilAdapter<ChipWidgetUiModel>() {
    init {
        delegatesManager.addDelegate(ExploreWidgetAdapterDelegate.Chips())
    }
    override fun areItemsTheSame(oldItem: ChipWidgetUiModel, newItem: ChipWidgetUiModel): Boolean {
        //TODO () change per id
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ChipWidgetUiModel, newItem: ChipWidgetUiModel): Boolean {
        return oldItem == newItem
    }
}
