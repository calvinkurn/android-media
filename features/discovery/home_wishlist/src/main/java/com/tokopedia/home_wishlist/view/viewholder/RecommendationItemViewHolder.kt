package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.topads.sdk.utils.ImpresionTask

class RecommendationItemViewHolder(view: View) : AbstractViewHolder<RecommendationItemDataModel>(view){

    private val productCardView: ProductCardViewSmallGrid by lazy { view.findViewById<ProductCardViewSmallGrid>(R.id.product_item) }

    override fun bind(element: RecommendationItemDataModel) {
        productCardView.run {
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

            setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener {
                override fun onViewHint() {
                    if(element.recommendationItem.isTopAds){
                        ImpresionTask().execute(element.recommendationItem.trackerImageUrl)
                    }

                }
            })

            setOnClickListener {

                if (element.recommendationItem.isTopAds) ImpresionTask().execute(element.recommendationItem.clickUrl)
            }

            setButtonWishlistOnClickListener {

            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_item
    }
}