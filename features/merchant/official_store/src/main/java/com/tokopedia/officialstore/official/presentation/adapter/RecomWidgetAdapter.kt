package com.tokopedia.officialstore.official.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem


class RecomWidgetAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recentViewItemHoldeDataList: List<RecommendationItem> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return RecommendationWidgetViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(RecommendationWidgetViewHolder.LAYOUT, parent, false)
        return RecommendationWidgetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recentViewItemHoldeDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as RecommendationWidgetViewHolder
        val data = recentViewItemHoldeDataList[position]
        holderView.bind(data)
    }

}
