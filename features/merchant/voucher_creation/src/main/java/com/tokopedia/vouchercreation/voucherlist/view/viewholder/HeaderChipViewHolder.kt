package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip
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

            setOnClickListener { onClick(element) }
            setChipState(element.isActive)

            itemChipMvc.chip_right_icon.setImageResource(R.drawable.ic_mvc_chevron_down)

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
                chip_right_icon.setColorFilter(ContextCompat.getColor(context, R.color.Green_G500))
            } else {
                chipType = ChipsUnify.TYPE_NORMAL
                chip_right_icon.setColorFilter(ContextCompat.getColor(context, R.color.Neutral_N400))
            }
        }
    }
}