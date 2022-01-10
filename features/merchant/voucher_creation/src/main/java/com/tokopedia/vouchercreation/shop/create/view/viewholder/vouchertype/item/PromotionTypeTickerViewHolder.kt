package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel
import kotlinx.android.synthetic.main.mvc_voucher_type_budget_ticker.view.*

class PromotionTypeTickerViewHolder(itemView: View) : AbstractViewHolder<PromotionTypeTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_type_budget_ticker
    }

    override fun bind(element: PromotionTypeTickerUiModel) {
        itemView.run {
            typeBudgetTickerDesc?.text = context?.resources?.getText(element.descRes) ?: ""
            typeBudgetTickerClose?.setOnClickListener {
                element.onDismissTicker()
            }
        }

    }
}