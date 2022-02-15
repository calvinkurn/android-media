package com.tokopedia.videoTabComponent.viewmodel

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.videoTabComponent.domain.delegate.PlaySlotTabViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.callback.PlaySlotTabCallback
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab

/**
 * Created by meyta.taliti on 28/01/22.
 */
class VideoTabAdapter(
    coordinator: PlayWidgetCoordinatorVideoTab,
    listener: PlaySlotTabCallback,
    position: Int
) : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
            .addDelegate(PlayWidgetViewAdapterDelegate.Jumbo(coordinator))
            .addDelegate(PlayWidgetViewAdapterDelegate.Large(coordinator))
            .addDelegate(PlayWidgetViewAdapterDelegate.Medium(coordinator))
            .addDelegate(PlaySlotTabViewAdapterDelegate.SlotTab(listener, position))
    }

    override fun areItemsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}