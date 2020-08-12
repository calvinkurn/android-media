package com.tokopedia.topads.dashboard.view.adapter.insight

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R


class TopAdsInsightTabAdapter(private val context: Context) : RecyclerView.Adapter<TopAdsTabInsightViewHolder>() {

    @LayoutRes
    private var itemLayout = R.layout.topads_dash_item_insight_tab_layout
    private var selectedTabPosition = 0
        private set
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

    fun setTabTitles(resources: Resources,countKey:Int,countBid:Int,countProduct:Int){
        tabMenus.clear()
        tabMenus.add(String.format(resources.getString(R.string.topads_dash_keyword_count),countKey))
        tabMenus.add(String.format(resources.getString(R.string.topads_dash_product_suggestion_insight_count),countProduct))
        tabMenus.add(String.format(resources.getString(R.string.topads_dash_bid_suggestion_insight_count),countBid))
        notifyDataSetChanged()
    }

    override fun getItemCount() = tabMenus.size

    interface OnRecyclerTabItemClick {
        fun onTabItemClick(position: Int)
    }

}
