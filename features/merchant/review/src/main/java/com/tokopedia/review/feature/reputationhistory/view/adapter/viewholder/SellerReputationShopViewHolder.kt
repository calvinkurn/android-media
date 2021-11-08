package com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder

import android.view.View
import android.widget.RelativeLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.reputationhistory.view.helper.ReputationView
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationShopUiModel
import com.tokopedia.unifyprinciples.Typography

class SellerReputationShopViewHolder(view: View,
                                     private val sellerReputationInfoListener: SellerReputationInfoListener): AbstractViewHolder<ReputationShopUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.seller_reputation_header
    }

    private val reputationView: ReputationView =
        itemView.findViewById(R.id.seller_reputation_header_view)

    private val rlReputationPointCalculation: RelativeLayout = itemView.findViewById(R.id.rl_reputation_point_calculation)

    private val textHeaderReputation: Typography = itemView.findViewById(R.id.text_header_reputation)

    override fun bind(element: ReputationShopUiModel) {
        reputationView.init(element)
        textHeaderReputation.text = getString(R.string.shop_reputation)
        rlReputationPointCalculation.setOnClickListener {
            sellerReputationInfoListener.onClickReputationInfo()
        }
    }

    interface SellerReputationInfoListener {
        fun onClickReputationInfo()
    }
}