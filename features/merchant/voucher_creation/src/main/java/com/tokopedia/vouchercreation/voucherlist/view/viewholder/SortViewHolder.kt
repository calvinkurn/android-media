package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.SortUiModel
import kotlinx.android.synthetic.main.item_mvc_sort.view.*

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class SortViewHolder(
        itemView: View?,
        private val onApplyClick: (sort: SortUiModel) -> Unit
) : AbstractViewHolder<SortUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_mvc_sort
    }

    override fun bind(element: SortUiModel) {
        with(itemView) {
            tvMvcSort.text = element.label
            radMvcSort.run{
                isChecked = element.isSelected
                //todo: add logic to notify adapter when checked changed
            }
            setOnClickListener {
                onApplyClick(element)
            }
        }
    }
}