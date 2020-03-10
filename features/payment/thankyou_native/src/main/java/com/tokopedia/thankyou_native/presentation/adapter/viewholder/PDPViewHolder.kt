package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.PDPItemAdapterModel
import kotlinx.android.synthetic.main.thank_item_recommendation.view.*

class PDPViewHolder(val view: View) : AbstractViewHolder<PDPItemAdapterModel>(view) {

    companion object {
        val LAYOUT_ID = R.layout.thank_item_recommendation
    }

    override fun bind(element: PDPItemAdapterModel?) {
        itemView.productCardView.apply {

            element?.let {
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
                                    ProductCardModel.ShopBadge(imageUrl = it ?: "")
                                },
                                freeOngkir = ProductCardModel.FreeOngkir(
                                        isActive = element.recommendationItem.isFreeOngkirActive,
                                        imageUrl = element.recommendationItem.freeOngkirImageUrl
                                )
                        )
                )
            }
            productCardView.setAddToCartVisible(true)
            setAddToCartOnClickListener {
                //actionListener?.onButtonAddToCartClicked(element)
            }
            setButtonWishlistOnClickListener {
                /*if (element.recommendationItem.isWishlist) {
                    actionListener?.onRemoveRecommendationFromWishlist(element.recommendationItem.productId.toString())
                } else {
                    actionListener?.onAddRecommendationToWishlist(element.recommendationItem.productId.toString())
                }*/
            }
        }
        itemView.setOnClickListener {
            // actionListener?.onRecommendationProductClicked(element.recommendationItem.productId.toString())
        }
    }

    fun clearImage() {
        itemView.productCardView.setImageProductVisible(false)
    }
}