package com.tokopedia.promousage.util.composite

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

internal class CompositeAdapter(
    private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items = mutableListOf<DelegateAdapterItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        this.delegates[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = this.delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            delegateAdapter.bindViewHolder(this.items[position], holder)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val delegateAdapter = delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            if (payloads.isEmpty()) {
                delegateAdapter.bindViewHolder(this.items[position], holder)
            } else {
                delegateAdapter.bindViewHolder(this.items[position], holder, payloads)
            }
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until this.delegates.size()) {
            if (this.delegates[i].modelClass == this.items[position].javaClass) {
                return this.delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun clear() {
        delegates.clear()
    }

    fun submit(newItems: List<DelegateAdapterItem>) {
        val diffCallback = DiffCallback(this.items.toList(), newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>> =
            SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out DelegateAdapterItem, *>): Builder {
            this.delegates.put(
                count++,
                delegateAdapter as? DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>
            )
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(this.delegates)
        }
    }

    inner class DiffCallback(
        private val oldItems: List<DelegateAdapterItem>,
        private val newItems: List<DelegateAdapterItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return oldItems[oldItemPosition].getChangePayload(newItems[newItemPosition])
        }
    }
}
