package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import android.widget.LinearLayout.*
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*

class RepurchaseSortFilterViewHolder(
    itemView: View
): AbstractViewHolder<RepurchaseSortFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_sort_filter
    }

    private val filterData = ArrayList<SortFilterItem>()
    private val sortFilter: SortFilter? by lazy { itemView.findViewById(R.id.sort_filter) }

    override fun bind(data: RepurchaseSortFilterUiModel) {
        addSortFilterItems(data)
        setupSortFilterLayout()
    }

    private fun addSortFilterItems(data: RepurchaseSortFilterUiModel) {
        data.sortFilterList.forEach {
            val title = getString(it.title)
            val item = SortFilterItem(title)
            val filterType = it.filterType

            filterData.add(item)
            sortFilter?.addItem(filterData)

            item.apply {
                type = it.chipType
                selectedItem = it.selectedItems
                listener = { onClickSortFilterItem(filterType) }
                refChipUnify.setChevronClickListener {
                    onClickSortFilterItem(filterType)
                }
            }
        }
    }

    private fun setupSortFilterLayout() {
        filterData.forEachIndexed { index, item ->
            if (index == 0) {
                val dimenMarginEnd = itemView.context.resources
                    .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                val dimenMarginStart = itemView.context.resources
                    .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

                val layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = dimenMarginStart
                    marginEnd = dimenMarginEnd
                }
                item.refChipUnify.layoutParams = layoutParams
            }
            item.refChipUnify.chip_right_icon.show()
        }
    }

    private fun onClickSortFilterItem(type: RepurchaseSortFilterType) {
        when(type) {
            RepurchaseSortFilterType.SORT -> openSortFilterBottomSheet()
            RepurchaseSortFilterType.DATE_FILTER -> openDateFilterBottomSheet()
            RepurchaseSortFilterType.CATEGORY_FILTER -> openCategoryFilterBottomSheet()
        }
    }

    private fun openSortFilterBottomSheet() {
    }

    private fun openDateFilterBottomSheet() {
    }

    private fun openCategoryFilterBottomSheet() {
    }
}