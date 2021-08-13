package com.tokopedia.navigation.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.Recommendation
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel

/**
 * Author errysuprayogi on 13,March,2019
 * Modify Lukas on July 31, 2019
 */
class RecommendationViewHolder(itemView: View, private val recommendationListener: RecommendationListener) : AbstractViewHolder<Recommendation>(itemView){
    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    override fun bind(element: Recommendation) {
        productCardView?.run {
            setProductModel(element.recommendationItem.toProductCardModel(hasThreeDots = true))
            setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener {
                override fun onViewHint() {
                    recommendationListener.onProductImpression(element.recommendationItem)
                }
            })

            setOnClickListener {
                recommendationListener.onProductClick(element.recommendationItem, null, adapterPosition)
            }

            setThreeDotsOnClickListener {
                recommendationListener.onThreeDotsClick(element.recommendationItem, adapterPosition)
            }
        }
    }

    override fun bind(element: Recommendation, payloads: MutableList<Any>) {
        val isWishlisted = payloads.getOrNull(0) as? Boolean ?: return

        element.recommendationItem.isWishlist = isWishlisted

        productCardView?.setThreeDotsOnClickListener {
            recommendationListener.onThreeDotsClick(element.recommendationItem, adapterPosition)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_recomendation
    }
}
