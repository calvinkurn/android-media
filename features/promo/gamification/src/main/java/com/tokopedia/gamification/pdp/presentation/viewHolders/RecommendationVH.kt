package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.presentation.GamiPdpRecommendationListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel

class RecommendationVH(itemView: View, val recommendationListener: GamiPdpRecommendationListener) : AbstractViewHolder<Recommendation>(itemView) {
    private val productCardView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

    override fun bind(element: Recommendation?) {
        if (element != null) {
            productCardView.run {
                setProductModel(getProductModel(element))
                        setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener {
                    override fun onViewHint() {
                        recommendationListener.onProductImpression(element.recommendationItem, adapterPosition)
                    }
                })

                setOnClickListener {
                    recommendationListener.onProductClick(element.recommendationItem, null, adapterPosition)
                }
            }
        }
    }

    private fun getProductModel(element: Recommendation): ProductCardModel {
        return ProductCardModel(
                slashedPrice = element.recommendationItem.slashedPrice,
                productName = element.recommendationItem.name,
                formattedPrice = element.recommendationItem.price,
                productImageUrl = element.recommendationItem.imageUrl,
                isTopAds = element.recommendationItem.isTopAds,
                discountPercentage = element.recommendationItem.discountPercentage.toString(),
                reviewCount = element.recommendationItem.countReview,
                ratingCount = element.recommendationItem.rating,
                shopLocation = element.recommendationItem.location,
                shopBadgeList = element.recommendationItem.badgesUrl.map {
                    ProductCardModel.ShopBadge(imageUrl = it
                            ?: "")
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                        isActive = element.recommendationItem.isFreeOngkirActive,
                        imageUrl = element.recommendationItem.freeOngkirImageUrl
                ),
                labelGroupList = element.recommendationItem.labelGroupList.map { recommendationLabel ->
                    ProductCardModel.LabelGroup(
                            position = recommendationLabel.position,
                            title = recommendationLabel.title,
                            type = recommendationLabel.type
                    )
                }
        )
    }

    companion object {
        val LAYOUT = com.tokopedia.gamification.R.layout.item_recommendation_pdp_gami
    }
}