package com.tokopedia.discovery2.viewcontrollers.adapter.mergeAdapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class MergeAdapters<T : RecyclerView.Adapter<AbstractViewHolder>>() : RecyclerView.Adapter<AbstractViewHolder>() {
    private val childAdapterList: ArrayList<LocalAdapter<T>> = ArrayList()
    private lateinit var mContext: Context
    private var mViewTypeIndex = 0


    constructor(context: Context) : this() {
        this.mContext = context;
    }

    fun addAdapter(childAdapter: T) {
//         New Implementation
        childAdapterList.add(LocalAdapter(childAdapter))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
//         New Implementation
        var viewHolder: AbstractViewHolder = childAdapterList[0].mAdapter.onCreateViewHolder(parent, 0)
        for (adapter in childAdapterList) {
            if (adapter.mViewTypesMap.containsKey(viewType)) {
                viewHolder = adapter.mAdapter.onCreateViewHolder(parent, adapter.mViewTypesMap[viewType]!!)
                return viewHolder
            }
        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        // New Implementation
        val result: LocalAdapter<*> = getActiveAdapter(position)
        setViewSpanType(holder)
        result.mAdapter.onBindViewHolder(holder, result.mLocalPosition)
    }


    override fun getItemViewType(position: Int): Int {
        // New Implementation
        val resultAdapter = getActiveAdapter(position)
        val localViewType = resultAdapter.mAdapter.getItemViewType(resultAdapter.mLocalPosition)
        if (resultAdapter.mViewTypesMap.containsValue(localViewType)) {
            for ((key, value) in resultAdapter.mViewTypesMap) {
                if (value == localViewType) {
                    return key
                }
            }
        }
        mViewTypeIndex += 1
        resultAdapter.mViewTypesMap[mViewTypeIndex] = localViewType
        return mViewTypeIndex
    }

    override fun getItemCount(): Int {
        // New Implementation
        return childAdapterList.sumBy { it.mAdapter.itemCount }
    }

    // New Implementation
    private fun getActiveAdapter(position: Int): LocalAdapter<*> {
        var currentChildAdapter = childAdapterList[0]
        val adapterCount: Int = childAdapterList.size
        var index = 0
        var listDataCount = 0
        while (index < adapterCount) {
            currentChildAdapter = childAdapterList[index]
            val newlistDataCount = listDataCount + currentChildAdapter.mAdapter.itemCount
            if (position < newlistDataCount) {

                currentChildAdapter.mLocalPosition = position - listDataCount
                return currentChildAdapter
            }
            listDataCount = newlistDataCount
            index++
        }
        return currentChildAdapter
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        holder.onViewDetachedToWindow()
        super.onViewDetachedFromWindow(holder)
    }

    // New Implementation
    private fun setViewSpanType(holder: AbstractViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when (holder) {
            is ProductCardItemViewHolder -> layoutParams.isFullSpan = false
            else -> layoutParams.isFullSpan = true
        }
    }

//    fun removeTabAdapter(tabListComponents: List<ComponentsItem>) {
//        if (!childAdapterList.isNullOrEmpty() && childAdapterList.size > 1) {
//            when (val adapter = childAdapterList[0].mAdapter) {
//                is DiscoveryRecycleAdapter -> adapter.removeExistingTabsComponent(tabListComponents)
//            }
//            childAdapterList.removeAt(1)
//            notifyDataSetChanged()
//        } else if (!childAdapterList.isNullOrEmpty()) {
//            when (val adapter = childAdapterList[0].mAdapter) {
//                is DiscoveryRecycleAdapter -> adapter.setDataRetainingOldData(tabListComponents as ArrayList<ComponentsItem>)
//            }
//        }
//    }
}