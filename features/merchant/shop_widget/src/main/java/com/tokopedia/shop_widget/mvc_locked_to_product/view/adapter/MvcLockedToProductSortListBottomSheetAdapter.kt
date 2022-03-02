package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel

class MvcLockedToProductSortListBottomSheetAdapter(
    typeFactory: MvcLockedToProductSortListBottomSheetTypeFactory
) : BaseListAdapter<Visitable<*>, MvcLockedToProductSortListBottomSheetTypeFactory>(typeFactory) {

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    fun addSortListData(data: List<MvcLockedToProductSortUiModel>) {
        val newList = getNewVisitableItems()
        newList.addAll(data)
        submitList(newList)
    }

    private fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = MvcLockedToProductSortListDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    fun setSelectedSortItem(
        sortData: MvcLockedToProductSortUiModel?,
        isSelected: Boolean
    ) {
        visitables.filterIsInstance<MvcLockedToProductSortUiModel>().find {
            it.value == sortData?.value
        }?.apply {
            this.isSelected = isSelected
            notifyItemChanged(visitables.indexOf(this))
        }
    }

    fun isSelectedSortAvailable(): Boolean {
        return visitables.filterIsInstance<MvcLockedToProductSortUiModel>().firstOrNull {
            it.isSelected
        } != null
    }

}