package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip
import kotlinx.android.synthetic.main.item_mvc_header_chip.view.*

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

    override fun bind(element: HeaderChip) {
        with(itemView) {
            itemChipMvc.run {
                chip_text.text = element.text
                chip_right_icon.visible()
                chip_right_icon.setOnClickListener {
                    onClick(element)
                }
            }

            setOnClickListener { onClick(element) }
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
        itemView.itemChipMvc.run {
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