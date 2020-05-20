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
        Log.d("onBindViewHolder ", position.toString())
        currentActiveAdapter?.onBindViewHolder(holder, position - lastListCount)
    }


    override fun getItemViewType(position: Int): Int {
        Log.d("getItemViewType ", position.toString())
        val x = lastListCount + currentActiveAdapter?.itemCount!!

        if (position >= x) {
            currentActiveAdapter = adapterList[++index]
            lastListCount = lastListCount + currentActiveAdapter?.itemCount!!
        } else if (position < lastListCount) {
            lastListCount = lastListCount - currentActiveAdapter?.itemCount!!
            currentActiveAdapter = adapterList[--index]
        }


        val id = currentActiveAdapter?.getItemViewType(position - lastListCount)
        return id ?: 0
    }


    override fun getItemCount(): Int {
        return adapterList.sumBy { it.itemCount }
    }


    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        currentActiveAdapter?.setDataList(dataList)
    }
}