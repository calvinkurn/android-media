package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel

/**
 * @author by jessica on 05/06/20
 */

class TrafficMetricViewHolder(view: View) : BaseViewHolder(view) {

    private val playSummaryInfoIcon = itemView.findViewById<ImageView>(R.id.iv_item_play_summary_info_icon)
    private val playSummaryInfoDescription = itemView.findViewById<TextView>(R.id.tv_item_play_summary_description)
    private val playSummaryInfoCount = itemView.findViewById<TextView>(R.id.tv_item_play_summary_count_info)

    fun bind(trafficMetricUiModel: TrafficMetricUiModel) {
        if (trafficMetricUiModel.trafficMetricEnum != null) {
            playSummaryInfoIcon.setImageResource(trafficMetricUiModel.trafficMetricEnum.thumbnailRes)
            playSummaryInfoDescription.text = itemView.resources.getString(trafficMetricUiModel.trafficMetricEnum.descriptionRes)
        }
        playSummaryInfoCount.text = trafficMetricUiModel.liveTrafficMetricCount
    }

    companion object {
        val LAYOUT = R.layout.item_play_summary_info
    }
}