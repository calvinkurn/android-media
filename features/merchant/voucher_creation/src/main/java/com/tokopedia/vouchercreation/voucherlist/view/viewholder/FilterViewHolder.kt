package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.BaseFilterUiModel.FilterItem
import kotlinx.android.synthetic.main.item_mvc_filter.view.*

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class FilterViewHolder(
        itemView: View?,
        private val onItemClick: () -> Unit
) : AbstractViewHolder<FilterItem>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_filter
    }

    override fun bind(element: FilterItem) {
        with(itemView) {
            tvMvcFilter.text = element.label
            cbxMvcFilter.isChecked = element.isSelected

            cbxMvcFilter.setOnCheckedChangeListener { _, isChecked ->
                element.isSelected = isChecked
                setOnCheckedChanged()
            }
            setOnClickListener {
                val isChecked = !cbxMvcFilter.isChecked
                cbxMvcFilter.isChecked = isChecked
            }
        }
    }

    private fun setOnCheckedChanged() {
        onItemClick()
    }
}