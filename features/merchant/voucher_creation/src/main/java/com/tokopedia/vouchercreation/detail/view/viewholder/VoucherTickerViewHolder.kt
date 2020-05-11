package com.tokopedia.vouchercreation.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.VoucherTickerUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_ticker.view.*

/**
 * Created By @ilhamsuaib on 10/05/20
 */

class VoucherTickerViewHolder(
        itemView: View?,
        private val onTooltipClick: () -> Unit
) : AbstractViewHolder<VoucherTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_ticker
    }

    override fun bind(element: VoucherTickerUiModel) {
        with(itemView.tickerMvcVoucher) {
            title = element.title
            description = element.description
            nominal = element.nominalStr
            if (element.hasTooltip) {
                setOnTooltipClick(onTooltipClick)
            }
        }
    }
}