package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.getColoredIndicator
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import kotlinx.android.synthetic.main.item_shop_score_penalty.view.*

class ItemPenaltyViewHolder(view: View) : AbstractViewHolder<ItemPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_score_penalty
    }

    override fun bind(element: ItemPenaltyUiModel?) {
        if (element == null) return

        with(itemView) {
            penaltyIndicator?.background = getColoredIndicator(context, element.colorPenalty)
            tv_title_status_penalty?.text = element.statusPenalty
            tv_end_date_status_penalty?.text = element.endDate
            tv_date_status_penalty?.text = element.statusDate
            tv_title_transaction_penalty?.apply {
                text = element.transactionPenalty
                if (element.colorPenalty.isNotBlank()) {
                    setTextColor(Color.parseColor(element.colorPenalty))
                }
            }
            tv_invoice_transaction_penalty?.text = element.descPenalty

            ic_transaction_penalty_to_detail?.setOnClickListener {
                //TODO
            }
        }
    }
}