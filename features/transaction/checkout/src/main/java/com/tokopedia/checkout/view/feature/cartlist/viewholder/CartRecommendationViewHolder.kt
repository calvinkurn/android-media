package com.tokopedia.checkout.view.feature.cartlist.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.item_cart_recommendation.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartRecommendationViewHolder(val view: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_cart_recommendation
    }

    fun bind(element: CartRecommendationItemHolderData) {
        itemView.productCardView.apply {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.recommendationItem.slashedPrice,
                            productName = element.recommendationItem.name,
                            formattedPrice = element.recommendationItem.price,
                            productImageUrl = element.recommendationItem.imageUrl,
                            isTopAds = element.recommendationItem.isTopAds,
                            discountPercentage = element.recommendationItem.discountPercentage.toString(),
                            reviewCount = element.recommendationItem.countReview,
                            ratingCount = element.recommendationItem.rating,
                            shopLocation = element.recommendationItem.location,
                            isWishlistVisible = true,
                            isWishlisted = element.recommendationItem.isWishlist,
                            shopBadgeList = element.recommendationItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.recommendationItem.isFreeOngkirActive,
                                    imageUrl = element.recommendationItem.freeOngkirImageUrl
                            )
                    )
            )
            setAddToCartVisible(true)
            setAddToCartOnClickListener {
                actionListener.onButtonAddToCartClicked(element)
            }
            setButtonWishlistOnClickListener {
                if (element.recommendationItem.isWishlist) {
                    actionListener.onRemoveRecommendationFromWishlist(element.recommendationItem.productId.toString())
                } else {
                    actionListener.onAddRecommendationToWishlist(element.recommendationItem.productId.toString())
                }
            }
        }
        itemView.setOnClickListener {
            actionListener.onRecommendationProductClicked(element.recommendationItem.productId.toString())
        }
    }

    fun clearImage() {
        itemView.productCardView.setImageProductVisible(false)
    }
}