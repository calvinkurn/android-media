package com.tokopedia.search.result.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.card.SmallGridInspirationCardViewHolder
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView

class ProductListAdapter(
    private val typeFactory: ProductListTypeFactory,
): RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private val list = mutableListOf<Visitable<*>>()
    private val loadingMoreModel = LoadingMoreModel()

    val itemList: List<Visitable<*>>
        get() = list

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
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = isStaggeredGridFullSpan(viewType)
        }
    }

    private fun isStaggeredGridFullSpan(viewType: Int): Boolean {
        return viewType != SmallGridProductItemViewHolder.LAYOUT
                && viewType != SmallGridProductItemViewHolder.LAYOUT_WITH_VIEW_STUB
                && viewType != RecommendationItemViewHolder.LAYOUT
                && viewType != SmallGridInspirationCardViewHolder.LAYOUT
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
}
