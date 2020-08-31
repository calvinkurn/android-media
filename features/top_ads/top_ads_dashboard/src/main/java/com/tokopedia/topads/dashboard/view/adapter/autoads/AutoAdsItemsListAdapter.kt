package com.tokopedia.topads.dashboard.view.adapter.autoads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.data.model.groupitem.DataItem
import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder.AutoAdsItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsViewModel

/**
 * Created by Pika on 2/6/20.
 */

class AutoAdsItemsListAdapter(private val typeFactory: AutoAdsItemsAdapterTypeFactory) : RecyclerView.Adapter<AutoAdsItemsViewHolder<AutoAdsItemsViewModel>>() {


    var items: MutableList<AutoAdsItemsViewModel> = mutableListOf()
    var statsData: MutableList<WithoutGroupDataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoAdsItemsViewHolder<AutoAdsItemsViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as AutoAdsItemsViewHolder<AutoAdsItemsViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: AutoAdsItemsViewHolder<AutoAdsItemsViewModel>, position: Int) {
        holder.bind(items[position],statsData)
    }

    fun setstatistics(data: List<WithoutGroupDataItem>) {
        statsData = data.toMutableList()
        notifyDataSetChanged()
    }
}


