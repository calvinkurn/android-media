package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricType
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel

/**
 * @author by jessica on 05/06/20
 */

class TrafficMetricViewHolder(view: View, val listener: Listener) : BaseViewHolder(view) {

    private val playSummaryInfoIcon = itemView.findViewById<ImageView>(R.id.iv_item_play_summary_info_icon)
    private val playSummaryInfoDescription = itemView.findViewById<TextView>(R.id.tv_item_play_summary_description)
    private val playSummaryInfoCount = itemView.findViewById<TextView>(R.id.tv_item_play_summary_count_info)

    fun bind(metric: TrafficMetricUiModel) {
        playSummaryInfoIcon.setImageResource(metric.type.icon)
        playSummaryInfoDescription.text = MethodChecker.fromHtml(getString(metric.type.label))
        playSummaryInfoCount.text = metric.count

        playSummaryInfoDescription.setOnClickListener {
            listener.onLabelClicked(metric.type)
        }
    }

    interface Listener {
        fun onLabelClicked(metricType: TrafficMetricType)
    }

    companion object {
        val LAYOUT = R.layout.item_play_summary_info
    }
}