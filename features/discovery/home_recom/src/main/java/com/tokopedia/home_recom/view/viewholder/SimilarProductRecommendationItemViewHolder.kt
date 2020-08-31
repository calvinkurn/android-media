package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.SimilarProductRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

/**
 * Created by Lukas on 30/08/19
 */
class SimilarProductRecommendationItemViewHolder (
        private val view: View
) : AbstractViewHolder<SimilarProductRecommendationItemDataModel>(view){

    private val productCardView: ProductCardGridView by lazy { view.findViewById<ProductCardGridView>(R.id.product_item) }
    override fun bind(element: SimilarProductRecommendationItemDataModel) {
        setupCard(element)
    }

    override fun bind(element: SimilarProductRecommendationItemDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && payloads.first() is Boolean){
            setupCard(element.copy(productItem = element.productItem.copy(isWishlist = payloads.first() as Boolean)))
        }
    }

    private fun setupCard(element: SimilarProductRecommendationItemDataModel){
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.productItem.slashedPrice,
                            productName = element.productItem.name,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            isTopAds = element.productItem.isTopAds,
                            isWishlistVisible = true,
                            hasThreeDots = true,
                            isWishlisted = element.productItem.isWishlist,
                            discountPercentage = element.productItem.discountPercentage,
                            reviewCount = element.productItem.countReview,
                            ratingCount = element.productItem.rating,
                            shopLocation = element.productItem.location,
                            shopBadgeList = element.productItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it
                                        ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.isFreeOngkirActive,
                                    imageUrl = element.productItem.freeOngkirImageUrl
                            ),
                            labelGroupList = element.productItem.labelGroupList.map {
                                ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type)
                            }
                    )
            )
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
}