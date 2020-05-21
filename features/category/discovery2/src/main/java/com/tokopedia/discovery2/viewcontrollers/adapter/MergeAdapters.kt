package com.tokopedia.discovery2.viewcontrollers.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class MergeAdapters : RecyclerView.Adapter<AbstractViewHolder>() {

    private val adapterList: ArrayList<DiscoveryRecycleAdapter> = ArrayList()
    private var currentActiveAdapter: DiscoveryRecycleAdapter? = null
    var index = 0
    var lastListCount = 0

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
//        Log.d("onBindViewHolder ", position.toString())
//        currentActiveAdapter?.onBindViewHolder(holder, position - lastListCount)

        Log.d("onBindViewHolder", position.toString())

        val pair = getActiveAdapter(position)
        pair.first.onBindViewHolder(holder, position - pair.second)
    }


    override fun getItemViewType(position: Int): Int {

        val pair = getActiveAdapter(position)


//        Log.d("getItemViewType ", position.toString())
//        val x = lastListCount + currentActiveAdapter?.itemCount!!
//
//        if (position >= x) {
//            currentActiveAdapter = adapterList[++index]
//            lastListCount = lastListCount + currentActiveAdapter?.itemCount!!
//        } else if (position < lastListCount) {
//            lastListCount = lastListCount - currentActiveAdapter?.itemCount!!
//            currentActiveAdapter = adapterList[--index]
//        }


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

//        adapterList.forEach {
//
//            if (xCount + it.itemCount > position) {
//                xAdapter = it
//
//            } else {
//                xCount += it.itemCount
//            }
//        }

        Log.d("getActiveAdapter", xAdapter.toString() + " postion " + position.toString() + "xCount" + xCount)
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
}