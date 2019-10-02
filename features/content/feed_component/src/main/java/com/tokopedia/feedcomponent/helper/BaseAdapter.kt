package com.tokopedia.feedcomponent.helper

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-01.
 */
abstract class BaseAdapter<T: Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val delegatesManager = AdapterDelegatesManager<T>()
    protected val itemList: MutableList<T> = mutableListOf()

    fun setItems(itemList: List<T>) {
        this.itemList.clear()
        addItems(itemList)
    }

    fun addItems(itemList: List<T>) {
        this.itemList.addAll(itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(itemList, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return delegatesManager.onBindViewHolder(itemList, position, holder)
    }
}