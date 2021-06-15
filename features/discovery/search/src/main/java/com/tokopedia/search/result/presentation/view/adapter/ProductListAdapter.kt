package com.tokopedia.search.result.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridInspirationCardViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class ProductListAdapter(
        private val itemChangeView: OnItemChangeView,
        private val typeFactory: ProductListTypeFactory,
): RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private val list = mutableListOf<Visitable<*>>()
    private val loadingMoreModel = LoadingMoreModel()

    val itemList get() = list

    fun changeSearchNavigationListView(position: Int) {
        if (position !in list.indices) return

        typeFactory.recyclerViewItem = SearchConstant.RecyclerView.VIEW_LIST
        notifyItemChanged(position, SearchConstant.RecyclerView.VIEW_LIST)
    }

    fun changeSearchNavigationDoubleGridView(position: Int) {
        if (position !in list.indices) return

        typeFactory.recyclerViewItem = SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID
        notifyItemChanged(position, SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID)
    }

    fun changeSearchNavigationSingleGridView(position: Int) {
        if (position !in list.indices) return

        typeFactory.recyclerViewItem = SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID
        notifyItemChanged(position, SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID)
    }

    fun changeListView() {
        typeFactory.recyclerViewItem = SearchConstant.RecyclerView.VIEW_LIST
        itemChangeView.onChangeList()
    }

    fun changeDoubleGridView() {
        typeFactory.recyclerViewItem = SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID
        itemChangeView.onChangeDoubleGrid()
    }

    fun changeSingleGridView() {
        typeFactory.recyclerViewItem = SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID
        itemChangeView.onChangeSingleGrid()
    }

    @StringRes
    fun getTitleTypeRecyclerView(): Int {
        return when (typeFactory.recyclerViewItem) {
            SearchConstant.RecyclerView.VIEW_LIST -> R.string.list
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> R.string.grid
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> R.string.grid
            else -> R.string.grid
        }
    }

    @DrawableRes
    fun getIconTypeRecyclerView(): Int {
        return when (typeFactory.recyclerViewItem) {
            SearchConstant.RecyclerView.VIEW_LIST -> R.drawable.search_ic_list
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> R.drawable.search_ic_grid
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> R.drawable.search_ic_big_list
            else -> R.drawable.search_ic_grid
        }
    }

    fun getCurrentLayoutType(): SearchConstant.ViewType {
        return when (typeFactory.recyclerViewItem) {
            SearchConstant.RecyclerView.VIEW_LIST -> SearchConstant.ViewType.LIST
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> SearchConstant.ViewType.SMALL_GRID
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> SearchConstant.ViewType.BIG_GRID
            else -> SearchConstant.ViewType.SMALL_GRID
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
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = isStaggeredGridFullSpan(viewType)
        }
    }

    private fun isStaggeredGridFullSpan(viewType: Int): Boolean {
        return viewType != SmallGridProductItemViewHolder.LAYOUT
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

    fun appendItems(list: List<Visitable<*>>) {
        val start = itemCount
        this.list.addAll(list)
        notifyItemRangeInserted(start, list.size)
    }

    fun updateWishlistStatus(productId: String, isWishlisted: Boolean) {
        list.forEachIndexed { i, visitable ->
            if (visitable is ProductItemDataView) {
                if (productId == visitable.productID) {
                    visitable.isWishlisted = isWishlisted
                    notifyItemChanged(i, "wishlist")
                }
            } else if (visitable is RecommendationItemDataView) {
                if (productId == visitable.recommendationItem.productId.toString()) {
                    visitable.recommendationItem.isWishlist = isWishlisted
                    notifyItemChanged(i, "wishlist")
                }
            } else if (visitable is BroadMatchDataView) {
                visitable.broadMatchItemDataViewList.forEach { broadMatchItemDataView ->
                    if (broadMatchItemDataView.id == productId) {
                        broadMatchItemDataView.isWishlisted = isWishlisted
                    }
                }
            }
        }
    }

    fun updateWishlistStatus(adapterPosition: Int, isWishlisted: Boolean) {
        if (adapterPosition !in list.indices) return

        val visitable = list[adapterPosition]

        if (visitable is ProductItemDataView) {
            visitable.isWishlisted = isWishlisted
            notifyItemChanged(adapterPosition)
        } else if (visitable is RecommendationItemDataView) {
            visitable.recommendationItem.isWishlist = isWishlisted
            notifyItemChanged(adapterPosition)
        }
    }

    fun isProductItem(position: Int): Boolean {
        return (position in list.indices) && list[position] is ProductItemDataView
    }

    fun isRecommendationItem(position: Int): Boolean {
        return (position in list.indices) && list[position] is RecommendationItemDataView
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

    fun showEmptyState(globalNavDataView: GlobalNavDataView?, emptySearchProductDataView: EmptySearchProductDataView) {
        clearData()

        if (globalNavDataView != null)
            list.add(globalNavDataView)

        list.add(emptySearchProductDataView)
        notifyItemRangeInserted(0, list.size)
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        list.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }

    fun isListEmpty() = list.isEmpty()

    fun removePriceFilterTicker() {
        val tickerIndex = list.indexOfFirst { it is TickerDataView }
        if (tickerIndex !in list.indices) return

        list.removeAt(tickerIndex)
        notifyItemRangeRemoved(tickerIndex, 1)
    }

    fun refreshItemAtIndex(index: Int) {
        if (index !in list.indices) return

        notifyItemChanged(index)
    }

    interface OnItemChangeView {
        fun onChangeList()
        fun onChangeDoubleGrid()
        fun onChangeSingleGrid()
    }
}