package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class ProductItemViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    fun bind(element: RecommendationItem) {
        with(itemView) {
            val card = this.findViewById<ProductCardGridView>(R.id.product_card_view)
            card.setProductModel(element.toProductCardModel(hasThreeDots = true))
            card.setImageProductViewHintListener(element, object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductRecommendationImpression(element, adapterPosition)
                }
            })

            card.setOnClickListener {
                listener.onProductRecommendationClicked(element, adapterPosition)
            }

            card.setThreeDotsOnClickListener {
                listener.onProductRecommendationThreeDotsClicked(element, adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_recommendation_item_product_card
    }
}