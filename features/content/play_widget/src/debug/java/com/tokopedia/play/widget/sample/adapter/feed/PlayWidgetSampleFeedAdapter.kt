package com.tokopedia.play.widget.sample.adapter.feed

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.sample.adapter.feed.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.play.widget.ui.model.PlayFeedUiModel

/**
 * Created by meyta.taliti on 28/01/22.
 */
class PlayWidgetSampleFeedAdapter : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
            .addDelegate(PlayWidgetViewAdapterDelegate.Jumbo())
            .addDelegate(PlayWidgetViewAdapterDelegate.Large())
            .addDelegate(PlayWidgetViewAdapterDelegate.Medium())
    }

    override fun areItemsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }
}