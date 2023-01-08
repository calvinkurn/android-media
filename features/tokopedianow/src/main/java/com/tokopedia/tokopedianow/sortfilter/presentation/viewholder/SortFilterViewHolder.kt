package com.tokopedia.tokopedianow.sortfilter.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSortFilterBinding
import com.tokopedia.tokopedianow.sortfilter.presentation.uimodel.SortFilterUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SortFilterViewHolder(
    itemView: View,
    val listener: SortFilterViewHolderListener
): AbstractViewHolder<SortFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_sort_filter
    }

    private var binding: ItemTokopedianowSortFilterBinding? by viewBinding()

    override fun bind(element: SortFilterUiModel) {
        binding?.apply {
            tpSortTitle.text = getString(element.titleRes.orZero())
            rbSort.isChecked = element.isChecked == true
            divider.showWithCondition(!element.isLastItem)
            container.setOnClickListener {
                listener.onClickSortItem(rbSort.isChecked, adapterPosition, element.value)
            }
            rbSort.setOnClickListener {
                listener.onClickSortItem(rbSort.isChecked, adapterPosition, element.value)
            }
        }
    }

    interface SortFilterViewHolderListener {
        fun onClickSortItem(isChecked: Boolean, position: Int, value: Int)
    }
}