package com.tokopedia.topads.dashboard.view.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.Summary
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TabLayoutViewHolder


/**
 * Created by hadi.putra on 15/05/18.
 */

class TopAdsTabAdapter(private val context: Context) : RecyclerView.Adapter<TabLayoutViewHolder>() {

    @LayoutRes
    private var itemLayout = R.layout.item_tab_layout
    var selectedTabPosition = 0
        private set
    private var listener: OnRecyclerTabItemClick? = null
    @TopAdsStatisticsType
    private var selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS

    private val tabMenus: MutableList<TabMenu> = mutableListOf()


    fun setSummary(summary: Summary?, subtitles: Array<String>) {
        if (summary == null) {
            tabMenus.clear()
            for (i in subtitles.indices) {
                tabMenus.add(TabMenu("-", if (i == INDEX_CONVERSION)
                    getStringConversion(selectedStatisticType)
                else
                    subtitles[i]))
            }
        } else {
            tabMenus.clear()
            tabMenus.add(TabMenu(summary.impressionSumFmt, subtitles[0]))
            tabMenus.add(TabMenu(summary.clickSumFmt, subtitles[1]))
            tabMenus.add(TabMenu(context.getString(R.string.top_ads_tooltip_statistic_use, summary.costSumFmt), subtitles[2]))
            tabMenus.add(TabMenu(context.getString(R.string.top_ads_tooltip_statistic_use, summary.grossProfitFmt), subtitles[3]))
            tabMenus.add(TabMenu(summary.ctrPercentageFmt, subtitles[4]))
            tabMenus.add(TabMenu(summary.conversionSumFmt, getStringConversion(selectedStatisticType)))
            tabMenus.add(TabMenu(context.getString(R.string.top_ads_tooltip_statistic_use, summary.costAvgFmt), subtitles[6]))
            tabMenus.add(TabMenu(summary.soldSumFmt, subtitles[7]))
        }
        notifyDataSetChanged()
    }

    private fun getStringConversion(selectedStatisticType: Int): String = when (selectedStatisticType) {
            TopAdsStatisticsType.SHOP_ADS ->  context.getString(R.string.label_top_ads_conversion_store)
            TopAdsStatisticsType.PRODUCT_ADS ->  context.getString(R.string.label_top_ads_conversion_product)
            TopAdsStatisticsType.ALL_ADS, TopAdsStatisticsType.HEADLINE_ADS ->  context.getString(R.string.label_top_ads_conversion)
            else ->  context.getString(R.string.label_top_ads_conversion)
        }

    fun setItemLayout(@LayoutRes itemLayout: Int) {
        this.itemLayout = itemLayout
    }

    fun setListener(listener: OnRecyclerTabItemClick) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabLayoutViewHolder {
        return TabLayoutViewHolder(LayoutInflater.from(parent.context).inflate(itemLayout, parent, false))
    }

    override fun onBindViewHolder(holder: TabLayoutViewHolder, position: Int) {
        holder.bind(tabMenus[position].mainTitle, tabMenus[position].subTitle)
        holder.itemView.setOnClickListener {
            selectedTabPosition = position
            listener?.onTabItemClick(position)
            notifyDataSetChanged()
        }
        holder.toggleActivate(position == selectedTabPosition)

    }

    override fun getItemCount() = tabMenus.size

    fun selected(position: Int) {
        selectedTabPosition = position
        notifyDataSetChanged()
    }

    fun setStatisticsType(@TopAdsStatisticsType selectedStatisticType: Int) {
        this.selectedStatisticType = selectedStatisticType
        tabMenus[INDEX_CONVERSION].subTitle = getStringConversion(selectedStatisticType)
        notifyDataSetChanged()
    }

    interface OnRecyclerTabItemClick {
        fun onTabItemClick(position: Int)
    }

    internal inner class TabMenu(var mainTitle: String, var subTitle: String)

    companion object {
        val INDEX_CONVERSION = 5
    }
}
