package com.tokopedia.topads.dashboard.view.adapter.insight

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R


class TopAdsInsightTabAdapter : RecyclerView.Adapter<TopAdsTabInsightViewHolder>() {

    @LayoutRes
    private var itemLayout = R.layout.topads_dash_item_insight_tab_layout
    private var selectedTabPosition = 0
    private var listener: OnRecyclerTabItemClick? = null
    private val tabMenus: MutableList<String> = mutableListOf()
    fun setListener(listener: OnRecyclerTabItemClick) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsTabInsightViewHolder {
        return TopAdsTabInsightViewHolder(LayoutInflater.from(parent.context).inflate(itemLayout, parent, false))
    }

    override fun onBindViewHolder(holder: TopAdsTabInsightViewHolder, position: Int) {
        holder.bind(tabMenus[position])
        holder.itemView.setOnClickListener {
            selectedTabPosition = position
            listener?.onTabItemClick(position)
            notifyDataSetChanged()
        }
        holder.toggleActivate(position == selectedTabPosition)
    }

    fun setTabTitles(resources: Resources, countProduct: Int, countBid: Int, countKey: Int) {
        tabMenus.clear()
        if (countProduct != 0)
            tabMenus.add(String.format(resources.getString(R.string.topads_dash_product_suggestion_insight_count), countProduct))
        if (countBid != 0)
            tabMenus.add(String.format(resources.getString(R.string.topads_dash_bid_suggestion_insight_count), countBid))
        if (countKey != 0)
            tabMenus.add(String.format(resources.getString(R.string.topads_dash_keyword_count), countKey))
        notifyDataSetChanged()
    }

    fun getTab(): MutableList<String> {
        return tabMenus
    }

    fun setSelectedTitle(index:Int){
        selectedTabPosition = index
        notifyDataSetChanged()
    }

    override fun getItemCount() = tabMenus.size

    interface OnRecyclerTabItemClick {
        fun onTabItemClick(position: Int)
    }

}
