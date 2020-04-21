package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.HeaderChipUiModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips.ChipType
import kotlinx.android.synthetic.main.item_mvc_header_chip.view.*

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class HeaderChipViewHolder(
        itemView: View?,
        private val onClick: (element: HeaderChipUiModel) -> Unit
) : AbstractViewHolder<HeaderChipUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_header_chip
    }

    override fun bind(element: HeaderChipUiModel) {
        with(itemView) {
            itemChipMvc.chip_text.text = element.text

            itemChipMvc.setChevronClickListener { onClick(element) }
            setOnClickListener { onClick(element) }
            setChipState(element.isActive)

            setupResetButton(element)
            setChipVisibility(element)
        }
    }

    private fun setChipVisibility(element: HeaderChipUiModel) = with(itemView) {
        if (!element.isVisible) {
            layoutParams.width = 0
            requestLayout()
        }
    }

    private fun setupResetButton(element: HeaderChipUiModel) = with(itemView) {
        if (element.type == ChipType.CHIP_RESET) {
            itemChipMvc.clearRightIcon()
            itemChipMvc.chip_image_icon.loadImageDrawable(R.drawable.ic_mvc_close)
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