package com.tokopedia.play.widget.sample

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleAdapter(
        coordinatorMap: Map<PlayWidgetCoordinator, PlayWidgetViewHolder?>
) : BaseDiffUtilAdapter<PlayWidgetUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
                .addDelegate(PlayWidgetViewAdapterDelegate(coordinatorMap))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetUiModel, newItem: PlayWidgetUiModel): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PlayWidgetUiModel, newItem: PlayWidgetUiModel): Boolean {
        return oldItem == newItem
    }
}