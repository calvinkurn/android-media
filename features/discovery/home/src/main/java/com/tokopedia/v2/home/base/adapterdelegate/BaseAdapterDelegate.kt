package com.tokopedia.v2.home.base.adapterdelegate

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterDelegate : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * variable [items]
     * is for hold list of data view
     */
    private val items: ArrayList<ModelViewType> = ArrayList()

//    /**
//     * [delegateAdapters]
//     * is a variable will hold all view type
//     */
//    abstract val delegateAdapters: SparseArrayCompat<ViewTypeDelegateAdapter>

    open val delegateManager = DelegateManager()

    /**
     * [diffUtil]
     * is a function for create callback diffutil helper
     * @param oldList is old list
     * @param newList is a new list (make sure object ref is different between old and new)
     */
    abstract fun diffUtil(oldList: List<ModelViewType>, newList: List<ModelViewType>): DiffUtil.Callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateManager.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        delegateAdapters.get(getItemViewType(position))?.onBindViewHolder(holder,  items[position]) ?: throw IllegalStateException("viewType is defined, but does not exist on adapter delegates")
        delegateManager.onBindViewHolder(holder, items[position], position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
//        if(payloads.isNotEmpty()) delegateAdapters.get(position)?.onBindViewHolder(holder, items[position], payloads)
        if(payloads.isNotEmpty()) delegateManager.onBindViewHolder(holder,  items[position], position, payloads)
        super.onBindViewHolder(holder, position, payloads)
    }

    /**
     * It will be return view reference
     */
    override fun getItemViewType(position: Int) = delegateManager.getItemViewType(items[position], position)

    fun submitList(newList: List<ModelViewType>){
        val diffResult = DiffUtil.calculateDiff(diffUtil(items, newList))
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newList)
    }
}