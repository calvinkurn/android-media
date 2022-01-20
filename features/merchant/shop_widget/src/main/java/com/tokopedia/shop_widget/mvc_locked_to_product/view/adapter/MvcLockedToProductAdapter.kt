package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.customview.OnStickySingleHeaderListener
import com.tokopedia.shop_widget.customview.StickySingleHeaderView
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductGridViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductTotalProductAndSortViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.*

class MvcLockedToProductAdapter(
    private val typeFactory: MvcLockedToProductTypeFactory
) : BaseListAdapter<Visitable<*>, MvcLockedToProductTypeFactory>(typeFactory),
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst {
            it::class.java == MvcLockedToProductTotalProductAndSortUiModel::class.java
        }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder? {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return typeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is MvcLockedToProductTotalProductAndSortViewHolder) {
            visitables.filterIsInstance(MvcLockedToProductTotalProductAndSortUiModel::class.java)
                .firstOrNull()?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {}

    override fun onStickyHide() {
        val newList = getNewVisitableItems()
        submitList(newList)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = isItemFullSpan(position)
        }
        super.onBindViewHolder(holder, position)
    }

    private fun isItemFullSpan(position: Int): Boolean {
        return getItemViewType(position) != MvcLockedToProductGridViewHolder.LAYOUT
    }

    fun addProductListData(mvcLockedToListProductGridProductUiModel: List<MvcLockedToProductGridProductUiModel>) {
        val newList = getNewVisitableItems()
        newList.addAll(mvcLockedToListProductGridProductUiModel)
        submitList(newList)
    }

    private fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = MvcLockedToProductDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    fun addVoucherData(mvcLockedToProductVoucherUiModel: MvcLockedToProductVoucherUiModel) {
        val newList = getNewVisitableItems()
        newList.add(mvcLockedToProductVoucherUiModel)
        submitList(newList)
    }

    fun addTotalProductAndSortData(mvcLockedToProductTotalProductAndSortUiModel: MvcLockedToProductTotalProductAndSortUiModel) {
        val newList = getNewVisitableItems()
        newList.add(mvcLockedToProductTotalProductAndSortUiModel)
        submitList(newList)
    }

    fun showInitialPagePlaceholderLoading() {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(MvcLockedToProductVoucherSortPlaceholderUiModel())
        newList.add(MvcLockedToProductGridListPlaceholderUiModel())
        submitList(newList)
    }

    override fun hideLoading() {
        val newList = getNewVisitableItems()
        hideLoadingMore(newList)
        hideVoucherSortPlaceholder(newList)
        hideProductGridListPlaceholder(newList)
        submitList(newList)
    }

    private fun hideVoucherSortPlaceholder(newList: MutableList<Visitable<*>>) {
        visitables.filterIsInstance<MvcLockedToProductVoucherSortPlaceholderUiModel>().firstOrNull()?.let {
            if (newList.contains(it)) {
                newList.remove(it)
            }
        }
    }

    private fun hideProductGridListPlaceholder(newList: MutableList<Visitable<*>>) {
        visitables.filterIsInstance<MvcLockedToProductGridListPlaceholderUiModel>().firstOrNull()?.let {
            if (newList.contains(it)) {
                newList.remove(it)
            }
        }
    }

    private fun hideLoadingMore(newList: MutableList<Visitable<*>>) {
        if (newList.contains(loadingMoreModel)) {
            newList.remove(loadingMoreModel)
        }
    }

    fun showLoadMoreLoading() {
        val newList = getNewVisitableItems()
        newList.add(loadingMoreModel)
        submitList(newList)
    }

    fun showGlobalErrorView(errorMessage: String, globalErrorType: Int) {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(
            MvcLockedToProductGlobalErrorUiModel(
                "",
                errorMessage,
                globalErrorType
            )
        )
        submitList(newList)
    }

    fun showNewProductListPlaceholder() {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<MvcLockedToProductGridProductUiModel>().let {
            newList.removeAll(it)
        }
        newList.add(MvcLockedToProductGridListPlaceholderUiModel())
        submitList(newList)
    }

    fun updateTotalProductAndSortData(selectedSortData: MvcLockedToProductSortUiModel) {
        val uiModel = visitables.filterIsInstance<MvcLockedToProductTotalProductAndSortUiModel>()
            .firstOrNull()
        uiModel?.let {
            it.selectedSortData = selectedSortData
        }
        val index = visitables.indexOf(uiModel)
        if (index >= 0 && index < visitables.size)
            notifyItemChanged(index)
    }

    fun getVoucherName(): String {
        return visitables.filterIsInstance<MvcLockedToProductVoucherUiModel>().firstOrNull()?.baseCode.orEmpty()
    }

    fun getFirstProductCardPosition(): Int {
        return visitables.indexOfFirst {
            it is MvcLockedToProductGridProductUiModel
        }
    }

}