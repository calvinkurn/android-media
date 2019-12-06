package com.tokopedia.home_wishlist.view.viewholder

import android.os.Bundle
import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.analytics.WishlistTracking
import com.tokopedia.home_wishlist.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.utils.ImpresionTask

class RecommendationItemViewHolder(view: View) : SmartAbstractViewHolder<RecommendationItemDataModel>(view){

    private val parentPositionDefault: Int = -1
    private val productCardView: ProductCardViewSmallGrid by lazy { view.findViewById<ProductCardViewSmallGrid>(R.id.product_item) }

    override fun bind(element: RecommendationItemDataModel, listener: SmartListener) {
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

            setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener{
                override fun onViewHint() {
                    if(element.recommendationItem.isTopAds){
                        ImpresionTask().execute(element.recommendationItem.trackerImageUrl)
                    }
                    (listener as WishlistListener).onProductImpression(element, adapterPosition)
                }
            })

            setOnClickListener {
                (listener as WishlistListener).onProductClick(element, parentPositionDefault, adapterPosition)
                if (element.recommendationItem.isTopAds) {
                    ImpresionTask().execute(element.recommendationItem.clickUrl)
                }
            }

            setButtonWishlistOnClickListener {
                (listener as WishlistListener).onWishlistClick(-1, adapterPosition, element.recommendationItem.isWishlist)
                WishlistTracking.clickEmptyWishlistIconRecommendation(
                        productId = element.recommendationItem.productId.toString(),
                        isAdd = !element.recommendationItem.isWishlist,
                        isTopAds = element.recommendationItem.isTopAds,
                        recomTitle = element.title
                )
            }
        }
    }

    override fun bind(element: RecommendationItemDataModel, listener: SmartListener, payloads: List<Any>) {
        if(payloads.isNotEmpty()){
            val bundle = payloads[0] as Bundle
            if(bundle.containsKey("wishlist")){
                productCardView.setButtonWishlistImage(bundle.getBoolean("wishlist"))
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_item
    }
}