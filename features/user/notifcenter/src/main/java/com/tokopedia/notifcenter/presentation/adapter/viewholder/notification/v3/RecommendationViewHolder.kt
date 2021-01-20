package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class RecommendationViewHolder constructor(
        itemView: View?,
        private val recommendationListener: RecommendationListener?
) : AbstractViewHolder<RecommendationUiModel>(itemView) {

    private val productCard: ProductCardGridView? = itemView?.findViewById(
            R.id.notification_product_card
    )

    override fun bind(element: RecommendationUiModel) {
        productCard?.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.recommendationItem.slashedPrice,
                            productName = element.recommendationItem.name,
                            formattedPrice = element.recommendationItem.price,
                            productImageUrl = element.recommendationItem.imageUrl,
                            isTopAds = element.recommendationItem.isTopAds,
                            discountPercentage = element.recommendationItem.discountPercentage,
                            reviewCount = element.recommendationItem.countReview,
                            ratingCount = element.recommendationItem.rating,
                            shopLocation = element.recommendationItem.location,
                            shopBadgeList = element.recommendationItem
                                    .badgesUrl
                                    .map {
                                        ProductCardModel.ShopBadge(imageUrl = it
                                                ?: "")
                                    },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.recommendationItem.isFreeOngkirActive,
                                    imageUrl = element.recommendationItem.freeOngkirImageUrl
                            ),
                            labelGroupList = element.recommendationItem
                                    .labelGroupList
                                    .map { recommendationLabel ->
                                        ProductCardModel.LabelGroup(
                                                position = recommendationLabel.position,
                                                title = recommendationLabel.title,
                                                type = recommendationLabel.type
                                        )
                                    },
                            hasThreeDots = true
                    )
            )
            setImageProductViewHintListener(element.recommendationItem, object : ViewHintListener {
                override fun onViewHint() {
                    recommendationListener?.onProductImpression(element.recommendationItem)
                }
            })

            setOnClickListener {
                recommendationListener?.onProductClick(
                        element.recommendationItem, null
                )
            }

            setThreeDotsOnClickListener {
                recommendationListener?.onThreeDotsClick(
                        element.recommendationItem, adapterPosition
                )
            }
        }
    }

    override fun bind(element: RecommendationUiModel, payloads: MutableList<Any>) {
        val isWishlisted = payloads.getOrNull(0) as? Boolean ?: return
        element.recommendationItem.isWishlist = isWishlisted
        productCard?.setThreeDotsOnClickListener {
            recommendationListener?.onThreeDotsClick(element.recommendationItem, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_recommendation
    }
}