package com.tokopedia.csat_rating.quickfilter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*


abstract class BaseQuickSingleFilterAdapter<T : BaseItemFilterViewHolder?> : RecyclerView.Adapter<T>() {

    protected var filterList: MutableList<QuickFilterItem>
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T
    abstract override fun onBindViewHolder(holder: T, position: Int)
    override fun getItemCount(): Int {
        return filterList.size
    }

    fun addQuickFilterList(filterList: List<QuickFilterItem>?) {
        this.filterList.clear()
        this.filterList.addAll(filterList!!)
        notifyDataSetChanged()
    }

    val dataList: List<QuickFilterItem>
        get() = filterList

    init {
        filterList = ArrayList()
    }
}