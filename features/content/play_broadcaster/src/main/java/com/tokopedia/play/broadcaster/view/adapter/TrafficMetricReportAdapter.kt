package com.tokopedia.play.broadcaster.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import kotlinx.android.synthetic.main.item_play_summary_info.view.*

/**
 * @author by jessica on 26/05/20
 */

class TrafficMetricReportAdapter : RecyclerView.Adapter<TrafficMetricReportAdapter.ViewHolder>() {

    private var trafficMetrics = mutableListOf<TrafficMetricUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(VH_LAYOUT, parent, false))

    override fun getItemCount(): Int = trafficMetrics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trafficMetrics[position])
    }

    fun addTrafficMetrics(trafficMetricUiModel: TrafficMetricUiModel) {
        this.trafficMetrics.add(trafficMetricUiModel)
        notifyDataSetChanged()
    }

    fun updateTrafficMetrics(trafficMetricUiModels: List<TrafficMetricUiModel>) {
        this.trafficMetrics.clear()
        this.trafficMetrics.addAll(trafficMetricUiModels)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(trafficMetricUiModel: TrafficMetricUiModel) {
            with(itemView) {
                if (trafficMetricUiModel.trafficMetricEnum != null) {
                    iv_item_play_summary_info_icon.setImageResource(trafficMetricUiModel.trafficMetricEnum.thumbnailRes)
                    tv_item_play_summary_description.text = resources.getString(trafficMetricUiModel.trafficMetricEnum.descriptionRes)
                }
                tv_item_play_summary_count_info.text = trafficMetricUiModel.liveTrafficMetricCount
            }
        }
    }

    companion object {
        val VH_LAYOUT = R.layout.item_play_summary_info
    }
}