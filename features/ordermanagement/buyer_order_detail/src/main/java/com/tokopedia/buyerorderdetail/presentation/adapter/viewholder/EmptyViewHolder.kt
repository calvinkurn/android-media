package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel

class EmptyViewHolder(itemView: View?): AbstractViewHolder<PGRecommendationWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.buyer_order_detail_empty_layout
    }

    override fun bind(element: PGRecommendationWidgetUiModel) {
        // noop
    }
}
