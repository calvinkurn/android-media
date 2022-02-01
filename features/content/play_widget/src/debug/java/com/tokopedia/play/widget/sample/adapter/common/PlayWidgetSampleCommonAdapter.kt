package com.tokopedia.play.widget.sample.adapter.common

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleCommonAdapter(
        coordinatorMap: Map<PlayWidgetCoordinator, PlayWidgetViewHolder?>
) : BaseDiffUtilAdapter<PlayWidgetState>(isFlexibleType = true) {

    init {
        delegatesManager
                .addDelegate(PlayWidgetSampleCommonAdapterDelegate(coordinatorMap))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetState, newItem: PlayWidgetState): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PlayWidgetState, newItem: PlayWidgetState): Boolean {
        return oldItem == newItem
    }
}