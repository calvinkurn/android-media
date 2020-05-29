package com.tokopedia.discovery2.viewcontrollers.adapter.mergeAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class MergeAdapters<T : RecyclerView.Adapter<AbstractViewHolder>> : RecyclerView.Adapter<AbstractViewHolder>() {

    private val adapterList: ArrayList<T> = ArrayList()
    private var initialChildAdapter: T? = null

    fun addAdapter(childAdapter: T) {
        if (adapterList.isEmpty()) {
            initialChildAdapter = childAdapter
        }
        adapterList.add(childAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return initialChildAdapter?.onCreateViewHolder(parent, viewType)!!
    }


    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        val adapterPositionPair = getActiveAdapter(position)
        setViewSpanType(holder)
        adapterPositionPair.first.onBindViewHolder(holder, position - adapterPositionPair.second)
    }

    private fun setViewSpanType(holder: AbstractViewHolder) {
        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        when (holder) {
            is ProductCardItemViewHolder -> layoutParams.isFullSpan = false
            else -> layoutParams.isFullSpan = true
        }
    }


    override fun getItemViewType(position: Int): Int {
        val adapterPositionPair = getActiveAdapter(position)
        return (if (position >= adapterPositionPair.second) {
            adapterPositionPair.first.getItemViewType(position - adapterPositionPair.second)
        } else {
            0
        })
    }

    override fun getItemCount(): Int {
        return adapterList.sumBy { it.itemCount }
    }

    private fun getActiveAdapter(position: Int): Pair<T, Int> {
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

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        holder.onViewDetachedToWindow()
        super.onViewDetachedFromWindow(holder)
    }
}