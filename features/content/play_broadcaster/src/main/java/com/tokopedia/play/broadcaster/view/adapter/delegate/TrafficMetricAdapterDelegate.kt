package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TrafficMetricViewHolder
import com.tokopedia.play_common.R as commonR

/**
 * @author by jessica on 05/06/20
 */

class TrafficMetricAdapterDelegate(
    private val listener: TrafficMetricViewHolder.Listener
) : TypedAdapterDelegate<TrafficMetricUiModel, TrafficMetricUiModel, TrafficMetricViewHolder>(commonR.layout.view_play_empty) {

    override fun onBindViewHolder(item: TrafficMetricUiModel, holder: TrafficMetricViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TrafficMetricViewHolder {
        return TrafficMetricViewHolder.create(parent, listener)
    }
}