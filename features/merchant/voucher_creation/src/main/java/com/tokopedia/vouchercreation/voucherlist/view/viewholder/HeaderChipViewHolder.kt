package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.BaseHeaderChipUiModel.HeaderChip
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
            itemChipMvc.chip_text.text = element.text

            itemChipMvc.setChevronClickListener { onClick(element) }
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
        itemView.itemChipMvc.chipType = if (isActive) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }
}