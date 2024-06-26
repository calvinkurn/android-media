package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel.Product
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder.OfferingProductListViewHolder
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder.OfferingProductSortingViewHolder
import com.tokopedia.buy_more_get_more.olp.utils.DataEndlessScrollListener
import com.tokopedia.buy_more_get_more.olp.view.widget.OnStickySingleHeaderListener
import com.tokopedia.buy_more_get_more.olp.view.widget.StickySingleHeaderView
import com.tokopedia.kotlin.extensions.view.toIntOrZero

open class OlpAdapter(
    private val adapterTypeFactory: AdapterTypeFactory
) : BaseListAdapter<Visitable<*>, AdapterTypeFactory>(adapterTypeFactory, null),
    DataEndlessScrollListener.OnDataEndlessScrollListener,
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    private var recyclerView: RecyclerView? = null
    private var productListUiModel: MutableList<Product> = mutableListOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = OlpDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<out Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = !(
                getItemViewType(position) == OfferingProductListViewHolder.LAYOUT ||
                    getItemViewType(position) == LoadingMoreViewHolder.LAYOUT
                )
        }
        super.onBindViewHolder(holder, position)
    }

    override fun getEndlessDataSize(): Int {
        return productListUiModel.size
    }

    override fun clearAllElements() {
        super.clearAllElements()
        refreshSticky()
    }

    private fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst {
            it::class.java == OfferProductSortingUiModel::class.java
        }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return adapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is OfferingProductSortingViewHolder) {
            visitables.filterIsInstance(OfferProductSortingUiModel::class.java).firstOrNull()?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun onStickyHide() {
        val newList = getNewVisitableItems()
        submitList(newList)
    }

    override fun showLoading() {
        if (!isLoading) {
            val newList = getNewVisitableItems()
            if (isShowLoadingMore) {
                newList.add(loadingMoreModel)
            } else {
                newList.add(loadingModel)
            }
            submitList(newList)
        }
    }

    override fun hideLoading() {
        val newList = getNewVisitableItems()
        if (visitables.contains(loadingModel)) {
            newList.remove(loadingModel)
        } else if (visitables.contains(loadingMoreModel)) {
            newList.remove(loadingMoreModel)
        }
        submitList(newList)
    }

    fun changeSelectedSortFilter(sortId: String, sortName: String) {
        val newList = getNewVisitableItems()
        val sortFilter = newList
            .filterIsInstance<OfferProductSortingUiModel>().firstOrNull()
        sortFilter?.apply {
            selectedSortId = sortId.toIntOrZero()
            selectedSortName = sortName
        }
        submitList(newList)
        refreshSticky()
    }

    fun updateProductCount(totalProducts: Int) {
        val newList = getNewVisitableItems()
        val sortFilter = newList
            .filterIsInstance<OfferProductSortingUiModel>().firstOrNull()
        sortFilter?.apply {
            productCount = totalProducts
        }
        submitList(newList)
        refreshSticky()
    }

    fun setProductCountVisibility(isVisible: Boolean) {
        val newList = getNewVisitableItems()
        val sortFilter = newList
            .filterIsInstance<OfferProductSortingUiModel>().firstOrNull()
        sortFilter?.apply {
            isProductCountVisible = isVisible
        }
        submitList(newList)
        refreshSticky()
    }

    fun removeProductList() {
        val newList = getNewVisitableItems()
        newList.removeAll(newList.filterIsInstance<Product>())
        productListUiModel.clear()
        submitList(newList)
    }

    fun setProductListData(productList: List<Product>) {
        val newList = getNewVisitableItems()
        productListUiModel.addAll(productList)
        newList.addAll(productList)
        submitList(newList)
    }
}
