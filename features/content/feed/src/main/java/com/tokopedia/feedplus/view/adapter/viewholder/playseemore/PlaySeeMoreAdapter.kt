package com.tokopedia.feedplus.view.adapter.viewholder.playseemore

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.videoTabComponent.domain.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab

/**
 * Created by jegul on 07/10/20
 */
class PlaySeeMoreAdapter(coordinator: PlayWidgetCoordinatorVideoTab
) : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {
    init {
        delegatesManager
                .addDelegate(PlayWidgetViewAdapterDelegate.Large(coordinator))

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