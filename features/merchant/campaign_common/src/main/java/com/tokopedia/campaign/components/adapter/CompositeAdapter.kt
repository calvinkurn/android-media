package com.tokopedia.campaign.components.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CompositeAdapter(
    private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<DelegateAdapterItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegates[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = delegates[getItemViewType(position)]

        println("Debug: Item position $position")
        if (delegateAdapter != null) {
            delegateAdapter.bindViewHolder(items[position], holder)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == items[position].javaClass) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun getItemCount() = items.size

    fun addItem(item: DelegateAdapterItem) {
        val newItems = getItems() + listOf(item)
        addItems(newItems)
    }

    fun addItems(items : List<DelegateAdapterItem>) {
        val newItems = getItems().toMutableList()
        newItems.addAll(items)
        submit(newItems)
    }

    fun removeItem(item: DelegateAdapterItem) {
        removeItems(listOf(item))
    }

    fun removeItems(items : List<DelegateAdapterItem>) {
        if (items.isNotEmpty()) {
            val newItems = this.items.toMutableList()
            newItems.removeAll(items)
            submit(newItems)
        }
    }

    fun submit(newItems: List<DelegateAdapterItem>) {
        val diffCallback = DiffCallback(this.items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }


    fun getItems(): List<DelegateAdapterItem> {
        return this.items
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


    inner class DiffCallback(
        private val oldProductList: List<DelegateAdapterItem>,
        private val newProductList: List<DelegateAdapterItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldProductList.size
        override fun getNewListSize() = newProductList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductList[oldItemPosition].id() == newProductList[newItemPosition].id()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  oldProductList[oldItemPosition] == newProductList[newItemPosition]
        }

    }
}