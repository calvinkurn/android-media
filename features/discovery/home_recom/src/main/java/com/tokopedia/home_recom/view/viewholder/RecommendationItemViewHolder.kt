package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Recommendation Item
 */
class RecommendationItemViewHolder(
       private val view: View
) : AbstractViewHolder<RecommendationItemDataModel>(view){

    private val productCardView: ProductCardGridView by lazy { view.findViewById<ProductCardGridView>(R.id.product_item) }

    override fun bind(element: RecommendationItemDataModel) {
        setupCard(element)
    }

    override fun bind(element: RecommendationItemDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && payloads.first() is Boolean){
            setupCard(element.copy(productItem = element.productItem.copy(isWishlist = payloads.first() as Boolean)))
        }
    }

    private fun setupCard(element: RecommendationItemDataModel){
        productCardView.run {
            setProductModel(setProductCardModel(element.productItem))

            setImageProductViewHintListener(element.productItem, object: ViewHintListener {
                override fun onViewHint() {
                    if(element.productItem.isTopAds){
                        TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                                this.javaClass.simpleName,
                                element.productItem.trackerImageUrl,
                                element.productItem.productId.toString(),
                                element.productItem.name,
                                element.productItem.imageUrl
                        )
                    }
                    element.listener.onProductImpression(element.productItem)
                }
            })

            setOnClickListener {
                element.listener.onProductClick(element.productItem, element.productItem.type, adapterPosition)
                if (element.productItem.isTopAds) TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                        this.javaClass.simpleName,
                        element.productItem.clickUrl,
                        element.productItem.productId.toString(),
                        element.productItem.name,
                        element.productItem.imageUrl
                )
            }

            setThreeDotsOnClickListener {
                element.listener.onThreeDotsClick(element.productItem, adapterPosition)
            }
        }
    }

    private fun setProductCardModel(recommendationItem: RecommendationItem) = ProductCardModel(
            slashedPrice = recommendationItem.slashedPrice,
            productName = recommendationItem.name,
            formattedPrice = recommendationItem.price,
            productImageUrl = recommendationItem.imageUrl,
            isTopAds = recommendationItem.isTopAds,
            isWishlistVisible = true,
            hasThreeDots = true,
            isWishlisted = recommendationItem.isWishlist,
            discountPercentage = recommendationItem.discountPercentage,
            reviewCount = recommendationItem.countReview,
            ratingCount = recommendationItem.rating,
            shopLocation = recommendationItem.location,
            shopBadgeList = recommendationItem.badgesUrl.map {
                ProductCardModel.ShopBadge(imageUrl = it
                        ?: "")
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                    isActive = recommendationItem.isFreeOngkirActive,
                    imageUrl = recommendationItem.freeOngkirImageUrl
            ),
            labelGroupList = recommendationItem.labelGroupList.map {
                ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type)
            }
    )
}
