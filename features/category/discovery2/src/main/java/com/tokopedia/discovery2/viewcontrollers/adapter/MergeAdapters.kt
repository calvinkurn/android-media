package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class MergeAdapters : RecyclerView.Adapter<AbstractViewHolder>() {

    private val adapterList: ArrayList<DiscoveryRecycleAdapter> = ArrayList()
    private var initialChildAdapter: DiscoveryRecycleAdapter? = null

    fun addAdapter(discoveryRecycleAdapter: DiscoveryRecycleAdapter) {
        if (adapterList.isEmpty()) {
            initialChildAdapter = discoveryRecycleAdapter
        }
        adapterList.add(discoveryRecycleAdapter)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return initialChildAdapter?.onCreateViewHolder(parent, viewType)!!
    }


    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        val adapterPositionPair = getActiveAdapter(position)
        adapterPositionPair.first.onBindViewHolder(holder, position - adapterPositionPair.second)
    }


    override fun getItemViewType(position: Int): Int {
        val adapterPositionPair = getActiveAdapter(position)

        return if (position >= adapterPositionPair.second) {
            adapterPositionPair.first.getItemViewType(position - adapterPositionPair.second)
        } else {
            0
        }
    }

    override fun getItemCount(): Int {
        return adapterList.sumBy { it.itemCount }
    }


    private fun getActiveAdapter(position: Int): Pair<DiscoveryRecycleAdapter, Int> {
        var listDataCount = 0
        var currentChildAdapter = adapterList[0]
        for (it in adapterList) {
            if (listDataCount + it.itemCount > position) {
                currentChildAdapter = it
                break
            } else {
                listDataCount += it.itemCount
            }
        }
        return Pair(currentChildAdapter, listDataCount)
    }

    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        initialChildAdapter?.setDataList(dataList)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()

    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        holder.onViewDetachedToWindow()
        super.onViewDetachedFromWindow(holder)
    }
}