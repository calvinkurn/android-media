package com.tokopedia.similarsearch.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.similarsearch.divider.DividerAdapterDelegate
import com.tokopedia.similarsearch.emptyresult.EmptyResultAdapterDelegate
import com.tokopedia.similarsearch.loadingmore.LoadingMoreAdapterDelegate
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.productitem.SimilarProductItemAdapterDelegate
import com.tokopedia.similarsearch.productitem.SimilarProductItemListener
import com.tokopedia.similarsearch.title.TitleAdapterDelegate

internal class SimilarSearchAdapter(
        similarProductItemListener: SimilarProductItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Any> = mutableListOf()
    private val adapterDelegatesManager = AdapterDelegatesManager()
            .addDelegate(SimilarProductItemAdapterDelegate(similarProductItemListener))
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
        adapterDelegatesManager.onBindViewHolder(list, holder, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
        else {
            adapterDelegatesManager.onBindViewHolder(list, holder, position, payloads)
        }
    }

    fun updateList(newList: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(SimilarSearchDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSimilarProductItemWishlistStatus(similarProductItem: Product) {
        val index = list.indexOfFirst { it is Product && it.id == similarProductItem.id }

        list[index] = similarProductItem

        notifyItemChanged(index, similarProductItem.isWishlisted)
    }
}