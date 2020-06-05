package com.tokopedia.discovery2.viewcontrollers.adapter.mergeAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class MergeAdapters : ListAdapter<ComponentsItem,AbstractViewHolder>(ComponentsDiffCallBacks()) {
    private val adapterList: ArrayList<DiscoveryRecycleAdapter> = ArrayList()
    private var currentActiveAdapter: DiscoveryRecycleAdapter? = null
    fun addAdapter(discoveryRecycleAdapter: DiscoveryRecycleAdapter) {
        if (adapterList.isEmpty()) {
            currentActiveAdapter = discoveryRecycleAdapter
        }
        adapterList.add(discoveryRecycleAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return currentActiveAdapter?.onCreateViewHolder(parent, viewType)!!
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        val pair = getActiveAdapter(position)
        setViewSpanType(holder)
        pair.first.onBindViewHolder(holder, position - pair.second)
    }

    override fun getItemViewType(position: Int): Int {
        val pair = getActiveAdapter(position)
        val id = pair.first.getItemViewType(position - pair.second)
        return id ?: 0
    }

    fun getActiveAdapter(position: Int): Pair<DiscoveryRecycleAdapter, Int> {
        var xCount = 0
        var xAdapter = adapterList[0]
        for (it in adapterList) {
            if (xCount + it.itemCount > position) {
                xAdapter = it
                break
            } else {
                xCount += it.itemCount
            }
        }
        return Pair(xAdapter, xCount)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        holder.onViewDetachedToWindow()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return adapterList.sumBy { it.itemCount }
    }

    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        currentActiveAdapter?.setDataList(dataList)
    }

    private fun setViewSpanType(holder: AbstractViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when (holder) {
            is ProductCardItemViewHolder -> layoutParams.isFullSpan = false
            else -> layoutParams.isFullSpan = true
        }
    }
}

class ComponentsDiffCallBacks : DiffUtil.ItemCallback<ComponentsItem>() {
    override fun areItemsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
        return oldItem?.id == newItem?.id //To change body of created functions use File | Settings | File Templates.
    }

    override fun areContentsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
        return newItem == oldItem
    }



}
