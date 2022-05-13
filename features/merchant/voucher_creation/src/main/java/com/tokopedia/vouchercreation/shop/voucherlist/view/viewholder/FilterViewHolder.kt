package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel.FilterItem

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class FilterViewHolder(
        itemView: View?,
        private val onItemClick: (String) -> Unit
) : AbstractViewHolder<FilterItem>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_filter
    }

    private var binding: ItemMvcFilterBinding? by viewBinding()

    override fun bind(element: FilterItem) {
        binding?.apply {
            tvMvcFilter.text = element.label
            cbxMvcFilter.isChecked = element.isSelected

            cbxMvcFilter.setOnCheckedChangeListener { _, isChecked ->
                element.isSelected = isChecked
                onItemClick(element.key)
            }
            root.setOnClickListener {
                val isChecked = !cbxMvcFilter.isChecked
                cbxMvcFilter.isChecked = isChecked
            }
        }
    }
}