package com.tokopedia.campaign.components.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CompositeAdapter(
    private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>>
) : ListAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == getItem(position).javaClass) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegates[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = delegates[getItemViewType(position)]


        if (delegateAdapter != null) {
            delegateAdapter.bindViewHolder(getItem(position), holder)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }


    fun addItem(item: DelegateAdapterItem) {
        addItems(listOf(item))
    }

    fun addItems(items: List<DelegateAdapterItem>) {
        val newItems = currentList.toMutableList()
        newItems.addAll(items)
        submitList(newItems)
    }

    fun removeItem(item: DelegateAdapterItem) {
        if (currentList.isNotEmpty()) {
            val newItems = currentList.toMutableList()
            newItems.remove(item)
            submitList(newItems)
        }
    }

    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>> = SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out DelegateAdapterItem, *>): Builder {
            delegates.put(count++, delegateAdapter as? DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>)
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(delegates)
        }
    }
}