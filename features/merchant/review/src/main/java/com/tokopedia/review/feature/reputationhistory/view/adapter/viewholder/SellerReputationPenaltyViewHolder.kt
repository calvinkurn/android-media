package com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationPenaltyUiModel
import com.tokopedia.unifyprinciples.Typography

class SellerReputationPenaltyViewHolder(view: View): AbstractViewHolder<ReputationPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_seller_reputation
    }

    private val tvDate: Typography = itemView.findViewById(R.id.tv_date)
    private val tvDesc: Typography = itemView.findViewById(R.id.tv_note)
    private val tvInvoice: Typography = itemView.findViewById(R.id.tv_invoice)
    private val tvScore: Typography = itemView.findViewById(R.id.tv_score)

    override fun bind(element: ReputationPenaltyUiModel?) {
        tvDate.text = element?.date
        tvDesc.text = element?.description
        tvInvoice.text = element?.invoice
        tvScore.text = element?.penaltyScore
    }
}