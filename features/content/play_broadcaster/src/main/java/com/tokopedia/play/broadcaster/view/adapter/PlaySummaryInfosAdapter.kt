package com.tokopedia.play.broadcaster.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.uimodel.SummaryUiModel
import kotlinx.android.synthetic.main.item_play_summary_info.view.*

/**
 * @author by jessica on 26/05/20
 */

class PlaySummaryInfosAdapter(private val trafficMetrics: List<SummaryUiModel.LiveTrafficMetric>) : RecyclerView.Adapter<PlaySummaryInfosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(VH_LAYOUT, parent, false))

    override fun getItemCount(): Int = trafficMetrics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trafficMetrics[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(trafficMetric: SummaryUiModel.LiveTrafficMetric) {
            with(itemView) {
                if (trafficMetric.liveTrafficMetricEnum != null) {
                    iv_item_play_summary_info_icon.setImageResource(trafficMetric.liveTrafficMetricEnum.thumbnailRes)
                    tv_item_play_summary_description.text = resources.getString(trafficMetric.liveTrafficMetricEnum.descriptionRes)
                } else {
                    iv_item_play_summary_info_icon.loadImage(trafficMetric.liveTrafficMetricIcon)
                    tv_item_play_summary_description.text = trafficMetric.liveTrafficMetricDescription
                }
                tv_item_play_summary_count_info.text = trafficMetric.liveTrafficMetricCount
            }
        }
    }

    companion object {
        val VH_LAYOUT = R.layout.item_play_summary_info
    }
}