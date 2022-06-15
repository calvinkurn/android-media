package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcHeaderChipBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class HeaderChipViewHolder(
        itemView: View?,
        private val onClick: (element: HeaderChip) -> Unit
) : AbstractViewHolder<HeaderChip>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_header_chip
    }

    private var binding: ItemMvcHeaderChipBinding? by viewBinding()

    override fun bind(element: HeaderChip) {
        binding?.apply {
            itemChipMvc.run {
                chip_text.text = element.text
                chip_right_icon.visible()
                chip_right_icon.setOnClickListener {
                    onClick(element)
                }
            }

            root.setOnClickListener { onClick(element) }
            setChipState(element.isActive)

            setChipVisibility(element)
        }
    }

    private fun setChipVisibility(element: HeaderChip) = with(itemView) {
        if (!element.isVisible) {
            layoutParams.width = 0
            requestLayout()
        }
    }

    private fun setChipState(isActive: Boolean) {
        binding?.let { binding ->
            binding.itemChipMvc.apply {
                if (isActive) {
                    chipType = ChipsUnify.TYPE_SELECTED
                    chip_right_icon.setImageResource(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_chevron_selected)
                } else {
                    chipType = ChipsUnify.TYPE_NORMAL
                    chip_right_icon.setImageResource(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_chevron_normal)
                }
            }
        }
    }
}