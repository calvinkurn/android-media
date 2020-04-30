package com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.widget

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeTickerUiModel
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