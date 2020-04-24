package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.BaseHeaderChipUiModel.ResetChip

/**
 * Created By @ilhamsuaib on 23/04/20
 */

class HeaderChipResetViewHolder(
        itemView: View?,
        private val onResetClick: (ResetChip) -> Unit
) : AbstractViewHolder<ResetChip>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_header_chip_reset
    }

    override fun bind(element: ResetChip) {
        with(itemView) {
            if (!element.isVisible) {
                layoutParams.width = 0
                requestLayout()
            }

            setOnClickListener {
                onResetClick(element)
                element.isVisible = false
            }
        }
    }
}