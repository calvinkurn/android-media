package com.tokopedia.play.widget.sample.adapter.feed

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.sample.adapter.feed.delegate.PlaySlotTabViewAdapterDelegate
import com.tokopedia.play.widget.sample.adapter.feed.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayFeedUiModel

/**
 * Created by meyta.taliti on 28/01/22.
 */
class PlayWidgetSampleFeedAdapter(
    coordinator: PlayWidgetCoordinator
) : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
            .addDelegate(PlayWidgetViewAdapterDelegate.Jumbo(coordinator))
            .addDelegate(PlayWidgetViewAdapterDelegate.Large(coordinator))
            .addDelegate(PlayWidgetViewAdapterDelegate.Medium(coordinator))
            .addDelegate(PlayWidgetViewAdapterDelegate.Small(coordinator))
            .addDelegate(PlaySlotTabViewAdapterDelegate.SlotTab())
    }

    override fun areItemsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}