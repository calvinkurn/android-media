package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcVoucherTickerBinding
import com.tokopedia.vouchercreation.shop.detail.model.VoucherTickerUiModel

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

    private var binding: ItemMvcVoucherTickerBinding? by viewBinding()

    override fun bind(element: VoucherTickerUiModel) {
        binding?.apply {
            tickerMvcVoucher.nominal = element.nominalStr
            if (element.hasTooltip) {
                tickerMvcVoucher.setOnTooltipClick(onTooltipClick)
            }
        }
    }
}