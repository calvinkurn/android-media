package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcVoucherTypeBudgetTickerBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel

class PromotionTypeTickerViewHolder(itemView: View) : AbstractViewHolder<PromotionTypeTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_type_budget_ticker
    }

    private var binding: MvcVoucherTypeBudgetTickerBinding? by viewBinding()

    override fun bind(element: PromotionTypeTickerUiModel) {
        binding?.apply {
            typeBudgetTickerDesc.text = binding?.root?.context?.resources?.getText(element.descRes) ?: ""
            typeBudgetTickerClose.setOnClickListener {
                element.onDismissTicker()
            }
        }

    }
}