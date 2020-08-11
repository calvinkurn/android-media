package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardModel
import kotlinx.android.synthetic.main.item_cart_recommendation.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartRecommendationViewHolder(view: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_cart_recommendation
    }

    fun bind(element: CartRecommendationItemHolderData) {
        itemView.productCardView?.apply {
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
                            },
                            hasAddToCartButton = true
                    )
            )
            setOnClickListener {
                actionListener?.onRecommendationProductClicked(
                        element.recommendationItem
                )
            }
            setAddToCartOnClickListener {
                actionListener?.onButtonAddToCartClicked(element)
            }

            setImageProductViewHintListener(
                    element.recommendationItem,
                    object : ViewHintListener {
                        override fun onViewHint() {
                            actionListener?.onRecommendationProductImpression(
                                    element.recommendationItem
                            )
                        }
                    }
            )
        }

        if (!element.hasSentImpressionAnalytics) {
            actionListener?.onRecommendationImpression(element)
            element.hasSentImpressionAnalytics = true
        }
    }

    fun clearImage() {
        itemView.productCardView?.recycle()
    }
}