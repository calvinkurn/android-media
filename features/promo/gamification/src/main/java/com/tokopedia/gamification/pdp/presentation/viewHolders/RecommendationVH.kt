package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.presentation.GamiPdpRecommendationListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView

class RecommendationVH(itemView: View, val recommendationListener: GamiPdpRecommendationListener) : AbstractViewHolder<Recommendation>(itemView) {
    private val productCardView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

    override fun bind(element: Recommendation?) {
        if (element != null) {
            productCardView.setProductModel(element.productCardModel)
            productCardView.setImageProductViewHintListener(element.recommendationItem, object : ViewHintListener {
                override fun onViewHint() {
                    recommendationListener.onProductImpression(element.recommendationItem, adapterPosition)
                }
            })

            productCardView.setOnClickListener {
                recommendationListener.onProductClick(element.recommendationItem, null, adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = com.tokopedia.gamification.R.layout.item_recommendation_pdp_gami
    }
}