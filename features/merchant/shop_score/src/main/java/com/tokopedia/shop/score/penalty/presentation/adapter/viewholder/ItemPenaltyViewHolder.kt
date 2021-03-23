package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import kotlinx.android.synthetic.main.item_shop_score_penalty.view.*

class ItemPenaltyViewHolder(view: View) : AbstractViewHolder<ItemPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_score_penalty
    }

    override fun bind(element: ItemPenaltyUiModel?) {
        if (element == null) return

        with(itemView) {
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            tv_title_status_penalty?.text = element.statusPenalty
            tv_end_date_status_penalty?.text = element.endDate
            tv_date_status_penalty?.text = element.statusDate
            tv_title_transaction_penalty?.text = element.transactionPenalty
            tv_invoice_transaction_penalty?.text = element.descPenalty

            ic_transaction_penalty_to_detail?.setOnClickListener {
                //TODO
            }
        }
    }
}