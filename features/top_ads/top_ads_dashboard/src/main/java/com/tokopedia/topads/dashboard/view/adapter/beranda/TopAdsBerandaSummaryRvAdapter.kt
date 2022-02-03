package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TopAdsBerandaSummaryRvAdapter :
    RecyclerView.Adapter<TopAdsBerandaSummaryRvAdapter.RingkasanViewHolder>() {

    private val list = mutableListOf<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Cell>()
    var infoClicked: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingkasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val holder = RingkasanViewHolder(view)
        holder.ivInformation.setOnClickListener { infoClicked?.invoke() }
        return holder
    }

    override fun onBindViewHolder(holder: RingkasanViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        with(holder) {

        }
    }

    fun addItems(items: List<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Cell>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class RingkasanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<Typography>(R.id.txtTitle)
        val ivInformation: ImageUnify = view.findViewById<ImageUnify>(R.id.ivInformation)
        val txtValue = view.findViewById<Typography>(R.id.txtValue)
        val txtPercentageChange = view.findViewById<Typography>(R.id.txtPercentageChange)
        val txtFromLastWeek = view.findViewById<Typography>(R.id.txtFromLastWeek)
    }

    companion object {
        private val layout = R.layout.item_rv_ringkasan
        fun createInstance() = TopAdsBerandaSummaryRvAdapter()
    }
}