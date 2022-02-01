package com.tokopedia.play.widget.sample.adapter.feed

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.sample.adapter.feed.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.play.widget.ui.model.PlayFeedUiModel

/**
 * Created by meyta.taliti on 28/01/22.
 */
class PlayWidgetSampleFeedAdapter(
    analyticListener: PlayWidgetAnalyticListener
) : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
            .addDelegate(PlayWidgetViewAdapterDelegate.Jumbo(analyticListener))
            .addDelegate(PlayWidgetViewAdapterDelegate.Large(analyticListener))
            .addDelegate(PlayWidgetViewAdapterDelegate.Medium(analyticListener))
            .addDelegate(PlayWidgetViewAdapterDelegate.SlotTab())
    }

    override fun areItemsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }
}