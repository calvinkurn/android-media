package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.TrafficMetricAdapterDelegate

/**
 * @author by jessica on 26/05/20
 */

class TrafficMetricReportAdapter : BaseDiffUtilAdapter<TrafficMetricUiModel>() {

    init {
        delegatesManager.addDelegate(TrafficMetricAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: TrafficMetricUiModel, newItem: TrafficMetricUiModel): Boolean {
        return (oldItem.metricLabel == newItem.metricLabel) && (oldItem.metricCount == newItem.metricCount)
    }

    override fun areContentsTheSame(oldItem: TrafficMetricUiModel, newItem: TrafficMetricUiModel): Boolean {
        return oldItem == newItem
    }

}