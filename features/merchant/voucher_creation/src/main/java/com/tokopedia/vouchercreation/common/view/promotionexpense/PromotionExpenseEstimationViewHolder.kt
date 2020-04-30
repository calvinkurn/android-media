package com.tokopedia.vouchercreation.common.view.promotionexpense

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.mvc_max_expense_estimation.view.*

class PromotionExpenseEstimationViewHolder(itemView: View): AbstractViewHolder<PromotionExpenseEstimationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_max_expense_estimation
    }

    override fun bind(element: PromotionExpenseEstimationUiModel) {
        itemView.run {
            if (!element.isHaveMargin) {
                setMargin(0,0,0,0)
            }
            estimationTooltip?.visibility =
                    if (element.isHaveToolTip) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            tvMvcExpense?.text = String.format(
                    context.resources.getString(R.string.mvc_rp_value),
                    CurrencyFormatHelper.convertToRupiah(element.estimationValue.toString()).toBlankOrString()).toBlankOrString()
        }
    }
}