package com.tokopedia.play.ui.explorewidget.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.explorewidget.PlayExploreWidgetCoordinator
import com.tokopedia.play.ui.explorewidget.adapter.delegate.WidgetAdapterDelegate
import com.tokopedia.play.view.uimodel.TabMenuUiModel
import com.tokopedia.play.view.uimodel.ExploreWidgetItemUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapter : //todo() add listener
    BaseDiffUtilAdapter<PlayWidgetItemUiModel>() { //todo() remove coordinator, etc, adjust analytics
    init {
        delegatesManager.addDelegate(WidgetAdapterDelegate.Widget())
        delegatesManager.addDelegate(WidgetAdapterDelegate.Shimmering())
    }

    override fun areItemsTheSame(oldItem: PlayWidgetItemUiModel, newItem: PlayWidgetItemUiModel): Boolean {
        return if (oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetItemUiModel, newItem: PlayWidgetItemUiModel): Boolean =
        oldItem == newItem
}
