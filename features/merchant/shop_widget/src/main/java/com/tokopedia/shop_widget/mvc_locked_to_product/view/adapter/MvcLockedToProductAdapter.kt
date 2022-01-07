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
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductProductGridViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductTotalProductAndSortViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductProductGridUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductTotalProductAndSortUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductVoucherUiModel

class MvcLockedToProductAdapter(
    private val typeFactory: MvcLockedToProductTypeFactory
) : BaseListAdapter<Visitable<*>, MvcLockedToProductTypeFactory>(typeFactory), StickySingleHeaderView.OnStickySingleHeaderAdapter {

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst {
            it::class.java == MvcLockedToProductTotalProductAndSortUiModel::class.java
        }
//    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder? {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return typeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is MvcLockedToProductTotalProductAndSortViewHolder) {
            visitables.filterIsInstance(MvcLockedToProductTotalProductAndSortUiModel::class.java).firstOrNull()?.let {
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
        return getItemViewType(position) != MvcLockedToProductProductGridViewHolder.LAYOUT
    }

    fun setProductListData(){
        val productList = mutableListOf<MvcLockedToProductProductGridUiModel>()
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        productList.add(MvcLockedToProductProductGridUiModel())
        val newList = getNewVisitableItems()
        newList.addAll(productList)
        submitList(newList)
    }

    private fun submitList(newList: List<Visitable<*>>) {
//        val currentRecyclerViewState: Parcelable? = recyclerView?.layoutManager?.onSaveInstanceState()
        val diffCallback = MvcLockedToProductDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
//        currentRecyclerViewState?.let{
//            recyclerView?.layoutManager?.onRestoreInstanceState(it)
//        }
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    fun setVoucherData() {
        val newList = getNewVisitableItems()
        newList.add(MvcLockedToProductVoucherUiModel())
        submitList(newList)
    }

    fun setTotalProductAndSortData() {
        val newList = getNewVisitableItems()
        newList.add(MvcLockedToProductTotalProductAndSortUiModel())
        submitList(newList)
    }

}