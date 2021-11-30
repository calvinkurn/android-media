package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TrafficMetricViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TrafficMetricAdapterDelegate

/**
 * @author by jessica on 26/05/20
 */

class TrafficMetricReportAdapter(listener: TrafficMetricViewHolder.Listener) : BaseDiffUtilAdapter<TrafficMetricUiModel>() {

    init {
        delegatesManager.addDelegate(TrafficMetricAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: TrafficMetricUiModel, newItem: TrafficMetricUiModel): Boolean {
        return (oldItem.type == newItem.type) && (oldItem.count == newItem.count)
    }

    override fun areContentsTheSame(oldItem: TrafficMetricUiModel, newItem: TrafficMetricUiModel): Boolean {
        return oldItem == newItem
    }

}