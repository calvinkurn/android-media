package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import kotlinx.android.synthetic.main.item_cart_recommendation.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartRecommendationViewHolder(view: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_cart_recommendation
    }

    fun bind(element: CartRecommendationItemHolderData) {
        itemView.productCardView.apply {
            setProductNameText(element.recommendationItem.name)
            setProductNameVisible(true)
            setLinesProductTitle(2)
            setPriceText(element.recommendationItem.price)
            setPriceVisible(true)
            setImageProductUrl(element.recommendationItem.imageUrl)
            setImageProductVisible(true)
            setImageTopAdsVisible(element.recommendationItem.isTopAds)
            setButtonWishlistImage(element.recommendationItem.isWishlist)
            setButtonWishlistVisible(true)
            setButtonWishlistOnClickListener {
                if (element.recommendationItem.isWishlist) {
                    actionListener.onRemoveRecommendationFromWishlist(element.recommendationItem.productId.toString())
                } else {
                    actionListener.onAddRecommendationToWishlist(element.recommendationItem.productId.toString())
                }
            }
            setRating(element.recommendationItem.rating)
            setImageRatingVisible(true)
            setReviewCount(element.recommendationItem.countReview)
            setReviewCountVisible(true)
            removeAllShopBadges()
            if (element.recommendationItem.badgesUrl.isNotEmpty()) {
                for (url in element.recommendationItem.badgesUrl) {
                    url?.also {
                        addShopBadge(generateBadgeView(it))
                    }
                }
                setShopBadgesVisible(true)
            } else {
                setShopBadgesVisible(false)
            }
            setShopLocationText(element.recommendationItem.location)
            setShopLocationVisible(true)
            setAddToCartVisible(true)
            setAddToCartOnClickListener {
                actionListener.onButtonAddToCartClicked(element)
            }
            realignLayout()
        }
        itemView.setOnClickListener {
            actionListener.onRecommendationProductClicked(element.recommendationItem.productId.toString())
        }
    }

    private fun generateBadgeView(url: String): View {
        val badgeView = LayoutInflater.from(itemView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, null)
        ImageHandler.loadImageFitCenter(itemView.context, badgeView.findViewById(com.tokopedia.productcard.R.id.badge), url)
        return badgeView
    }

    fun clearImage() {
        itemView.productCardView.setImageProductVisible(false)
    }
}