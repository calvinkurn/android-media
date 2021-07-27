package com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.reputationhistory.view.helper.ReputationView
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationShopUiModel

class SellerReputationShopViewHolder(view: View) : AbstractViewHolder<ReputationShopUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.seller_reputation_header
    }

    private var reputationView: ReputationView =
        itemView.findViewById(R.id.seller_reputation_header_view)

    override fun bind(element: ReputationShopUiModel) {
        reputationView.init(element)
    }
}