package com.tokopedia.search.result.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.PercentageScrollListener
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.utils.isFullSpan

class ProductListAdapter(
    private val typeFactory: ProductListTypeFactory,
): RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private val list = mutableListOf<Visitable<*>>()
    private val loadingMoreModel = LoadingMoreModel()

    val itemList: List<Visitable<*>>
        get() = list

    private val percentageScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        PercentageScrollListener()
    }
    private var recyclerView: RecyclerView? = null

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView?.removeOnScrollListener(percentageScrollListener)
        this.recyclerView = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<*>) {
        super.onViewAttachedToWindow(holder)
        if (holder is IAdsViewHolderTrackListener) {
            val item = list.get(holder.absoluteAdapterPosition)
            (holder as AbstractViewHolder<Visitable<*>>).onViewAttachedToWindow(item)
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<*>) {
        super.onViewDetachedFromWindow(holder)
        if (holder is IAdsViewHolderTrackListener) {
            val item = list.get(holder.absoluteAdapterPosition)
            (holder as AbstractViewHolder<Visitable<*>>).onViewDetachedFromWindow(item, holder.visiblePercentage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        setFullSpanForStaggeredGrid(holder, holder.itemViewType)

        @Suppress("UNCHECKED_CAST")
        (holder as AbstractViewHolder<Visitable<*>>).bind(list[position])
    }

    private fun setFullSpanForStaggeredGrid(holder: AbstractViewHolder<*>, viewType: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams)
            layoutParams.isFullSpan = isFullSpan(viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            @Suppress("UNCHECKED_CAST")
            (holder as AbstractViewHolder<Visitable<*>>).bind(list[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        return (list[position] as Visitable<ProductListTypeFactory>).type(typeFactory)
    }

    override fun getItemCount() = list.size

    override fun onViewRecycled(holder: AbstractViewHolder<*>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun appendItems(list: List<Visitable<*>>) {
        val start = itemCount
        this.list.addAll(list)
        notifyItemRangeInserted(start, list.size)
    }

    fun addLoading() {
        val loadingModelPosition = list.size
        list.add(loadingMoreModel)
        notifyItemInserted(loadingModelPosition)
    }

    fun removeLoading() {
        val loadingModelPosition = list.indexOf(loadingMoreModel)
        if (loadingModelPosition != -1) {
            list.remove(loadingMoreModel)
            notifyItemRemoved(loadingModelPosition)
            notifyItemRangeChanged(loadingModelPosition, 1)
        }
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        list.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }

    fun isListEmpty() = list.isEmpty()

    fun refreshItemAtIndex(index: Int) {
        if (index !in list.indices) return

        notifyItemChanged(index)
    }

    fun refreshItemAtIndex(index: Int, refreshItem: Visitable<*>) {
        list[index] = refreshItem
        notifyItemChanged(index)
    }

    fun removeLastFilterWidget() {
        val lastFilterWidgetIndex = list.indexOfFirst { it is LastFilterDataView }
        removeItem(lastFilterWidgetIndex)
    }

    private fun removeItem(index: Int) {
        if (index !in list.indices) return

        list.removeAt(index)
        notifyItemRangeRemoved(index, 1)
    }

    fun removeFirstItemWithCondition(condition: (Visitable<*>) -> Boolean) {
        removeItem(list.indexOfFirst { condition(it) })
    }

    fun insertItemAfter(visitable: Visitable<*>, previousVisitable: Visitable<*>) : Int {
        val previousItemIndex = list.indexOfFirst { it == previousVisitable }
        if(previousItemIndex == -1) return -1
        val targetIndex = previousItemIndex + 1
        list.add(targetIndex, visitable)
        notifyItemInserted(targetIndex)
        return targetIndex
    }

    fun unBlurItem() {
        list.forEachIndexed { index, visitable ->
            if(visitable is ProductItemDataView && visitable.isImageBlurred) {
                visitable.isImageBlurred = false
                notifyItemChanged(index)
            }
        }
    }
}
