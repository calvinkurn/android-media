package com.tokopedia.similarsearch

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

internal class SimilarSearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Any> = mutableListOf()
    private val adapterDelegatesManager = AdapterDelegatesManager()
            .addDelegate(SimilarProductItemAdapterDelegate())
            .addDelegate(DividerAdapterDelegate())
            .addDelegate(TitleAdapterDelegate())
            .addDelegate(LoadingMoreAdapterDelegate())
            .addDelegate(EmptyResultAdapterDelegate())

    override fun getItemViewType(position: Int): Int {
        return adapterDelegatesManager.getItemViewType(list, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapterDelegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return adapterDelegatesManager.onBindViewHolder(list, holder, position)
    }

    fun updateList(newList: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(SimilarSearchDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }
}