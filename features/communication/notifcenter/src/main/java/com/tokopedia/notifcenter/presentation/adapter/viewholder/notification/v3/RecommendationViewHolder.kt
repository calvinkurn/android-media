package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class RecommendationViewHolder constructor(
        itemView: View?,
        private val recommendationListener: RecommendationListener?
) : AbstractViewHolder<RecommendationUiModel>(itemView) {

    private val productCard: ProductCardGridView? = itemView?.findViewById(
            R.id.notification_product_card
    )

    override fun bind(element: RecommendationUiModel, payloads: MutableList<Any>) {
        val isWishlisted = payloads.getOrNull(0) as? Boolean ?: return
        element.recommendationItem.isWishlist = isWishlisted
        productCard?.setThreeDotsOnClickListener {
            recommendationListener?.onThreeDotsClick(element.recommendationItem, adapterPosition)
        }
    }

    override fun bind(element: RecommendationUiModel) {
        bindProductCardUi(element)
        bindProductCardImpression(element)
        bindProductCardClick(element)
        bindProductCardThreeDotsClick(element)
    }

    private fun bindProductCardUi(element: RecommendationUiModel) {
        productCard?.setProductModel(element.recommendationItem.toProductCardModel())
    }

    private fun bindProductCardImpression(element: RecommendationUiModel) {
        productCard?.setImageProductViewHintListener(
                element.recommendationItem,
                object : ViewHintListener {
                    override fun onViewHint() {
                        recommendationListener?.onProductImpression(element.recommendationItem)
                    }
                }
        )
    }

    private fun bindProductCardClick(element: RecommendationUiModel) {
        productCard?.setOnClickListener {
            recommendationListener?.onProductClick(
                    element.recommendationItem, null
            )
        }
    }

    private fun bindProductCardThreeDotsClick(element: RecommendationUiModel) {
        productCard?.setThreeDotsOnClickListener {
            recommendationListener?.onThreeDotsClick(
                    element.recommendationItem, adapterPosition
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_recommendation
    }
}