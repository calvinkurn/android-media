package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class LatestReadingTopAdsDashboardRvAdapter :
    RecyclerView.Adapter<LatestReadingTopAdsDashboardRvAdapter.LatestReadingViewHolder>() {

    private val list = mutableListOf<TopAdsLatestReading.TopAdsLatestReadingItem.Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestReadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_latest_reading, parent, false)
        return LatestReadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: LatestReadingViewHolder, position: Int) {
        val item = list[holder.adapterPosition]

        with(holder) {
            txtTitle.text = item.title
            txtDescription.text = item.description
            creditHistoryImage.setImageUrl(item.thumbnail)
        }
    }

    fun addItems(data: List<TopAdsLatestReading.TopAdsLatestReadingItem.Article>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class LatestReadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val creditHistoryImage: ImageUnify = view.findViewById(R.id.creditHistoryImage)
        val txtTitle: Typography = view.findViewById(R.id.txtTitle)
        val txtDescription: Typography = view.findViewById(R.id.txtDescription)
    }
}