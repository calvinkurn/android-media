package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecommendationItemHolderData
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
        itemView.productCardView.setProductNameText(element.recommendationItem.name)
        itemView.productCardView.setProductNameVisible(true)
        itemView.productCardView.setLinesProductTitle(2)
        itemView.productCardView.setPriceText(element.recommendationItem.price)
        itemView.productCardView.setPriceVisible(true)
        itemView.productCardView.setImageProductUrl(element.recommendationItem.imageUrl)
        itemView.productCardView.setImageProductVisible(true)
        itemView.productCardView.setImageTopAdsVisible(element.recommendationItem.isTopAds)
        itemView.productCardView.setButtonWishlistVisible(true)
        itemView.productCardView.setButtonWishlistImage(element.recommendationItem.isWishlist)
        itemView.productCardView.setButtonWishlistOnClickListener {
            if (element.recommendationItem.isWishlist) {
                actionListener.onRemoveFromWishlist(element.recommendationItem.productId.toString())
            } else {
                actionListener.onAddToWishlist(element.recommendationItem.productId.toString())
            }
        }
        itemView.productCardView.setRating(element.recommendationItem.rating)
        itemView.productCardView.setImageRatingVisible(true)
        itemView.productCardView.setReviewCount(element.recommendationItem.countReview)
        itemView.productCardView.setReviewCountVisible(true)
        itemView.productCardView.removeAllShopBadges()
        if (element.recommendationItem.badgesUrl.isNotEmpty()) {
            for (url in element.recommendationItem.badgesUrl) {
                val view = LayoutInflater.from(itemView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, null)
                ImageHandler.loadImageFitCenter(itemView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), url)
                itemView.productCardView.addShopBadge(view)
            }
            itemView.productCardView.setShopBadgesVisible(true)
        } else {
            itemView.productCardView.setShopBadgesVisible(false)
        }
        itemView.productCardView.setShopLocationText(element.recommendationItem.location)
        itemView.productCardView.setShopLocationVisible(true)
        itemView.productCardView.setAddToCartVisible(true)
        itemView.productCardView.setAddToCartOnClickListener {
            actionListener.onButtonAddToCartClicked(element)
        }
        itemView.productCardView.realignLayout()
        itemView.setOnClickListener {
            actionListener.onRecommendationProductClicked(element.recommendationItem.productId.toString())
        }
    }

    fun clearImage() {
        itemView.productCardView.setImageProductVisible(false)
    }
}