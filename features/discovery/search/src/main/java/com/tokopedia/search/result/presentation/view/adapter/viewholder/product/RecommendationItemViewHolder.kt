package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel

class RecommendationItemViewHolder (
        itemView: View,
        val listener: RecommendationListener
) : AbstractViewHolder<RecommendationItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_recommendation_card_small_grid
    }

    private val productCardViewHintListener: ProductCardView? by lazy{
        itemView.findViewById<ProductCardView>(R.id.productCardView)
    }

    override fun bind(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.apply {
            val recommendationItem = recommendationItemViewModel.recommendationItem
            setProductModel(
                    ProductCardModel(
                            slashedPrice = recommendationItem.slashedPrice,
                            productName = recommendationItem.name,
                            formattedPrice = recommendationItem.price,
                            productImageUrl = recommendationItem.imageUrl,
                            isTopAds = recommendationItem.isTopAds,
                            discountPercentage = recommendationItem.discountPercentage.toString(),
                            reviewCount = recommendationItem.countReview,
                            ratingCount = recommendationItem.rating,
                            shopLocation = recommendationItem.location,
                            isWishlistVisible = true,
                            isWishlisted = recommendationItem.isWishlist,
                            shopBadgeList = recommendationItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = recommendationItem.isFreeOngkirActive,
                                    imageUrl = recommendationItem.freeOngkirImageUrl
                            )
                    )
            )
            setButtonWishlistOnClickListener {
                listener.onWishlistClick(recommendationItem, recommendationItem.isWishlist){ success, throwable ->
                    /** do nothing **/
                }
            }

            setOnClickListener {
                listener.onProductClick(recommendationItem, "", adapterPosition)
            }

            setImageProductViewHintListener(recommendationItemViewModel, createImageProductViewHintListener(recommendationItemViewModel))
        }
    }

    private fun createImageProductViewHintListener(recommendationItemViewModel: RecommendationItemViewModel): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                listener.onProductImpression(recommendationItemViewModel.recommendationItem)
            }
        }
    }

}