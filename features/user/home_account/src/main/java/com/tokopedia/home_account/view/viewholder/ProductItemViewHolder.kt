package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class ProductItemViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    fun bind(element: RecommendationItem) {
        with(itemView) {
            val card = this.findViewById<ProductCardGridView>(R.id.product_card_view)
            card.setProductModel(ProductCardModel(
                    slashedPrice = element.slashedPrice,
                    productName = element.name,
                    formattedPrice = element.price,
                    productImageUrl = element.imageUrl,
                    isTopAds = element.isTopAds,
                    discountPercentage = element.discountPercentage,
                    reviewCount = element.countReview,
                    ratingCount = element.rating,
                    shopLocation = element.location,
                    isWishlistVisible = true,
                    isWishlisted = element.isWishlist,
                    shopBadgeList = element.badgesUrl.map {
                        ProductCardModel.ShopBadge(imageUrl = it ?: "")
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = element.isFreeOngkirActive,
                            imageUrl = element.freeOngkirImageUrl
                    ),
                    labelGroupList = element.labelGroupList.map { recommendationLabel ->
                        ProductCardModel.LabelGroup(
                                position = recommendationLabel.position,
                                title = recommendationLabel.title,
                                type = recommendationLabel.type
                        )
                    },
                    hasThreeDots = true
            ))

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