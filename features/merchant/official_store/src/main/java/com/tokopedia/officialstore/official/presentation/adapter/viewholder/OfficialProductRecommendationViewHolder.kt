package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter


class OfficialProductRecommendationViewHolder(
        view: View,
        val recommendationListener: RecommendationListener
): AbstractViewHolder<ProductRecommendationViewModel>(view) {

    private val productCardView: ProductCardGridView? by lazy { view.findViewById<ProductCardGridView>(R.id.product_item) }

    override fun bind(element: ProductRecommendationViewModel) {
        productCardView?.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.productItem.slashedPrice,
                            productName = element.productItem.name,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            isTopAds = element.productItem.isTopAds,
                            discountPercentage = element.productItem.discountPercentage.toString(),
                            reviewCount = element.productItem.countReview,
                            ratingCount = element.productItem.rating,
                            shopLocation = element.productItem.shopName,
                            isWishlistVisible = true,
                            isWishlisted = element.productItem.isWishlist,
                            shopBadgeList = element.productItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it
                                        ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.isFreeOngkirActive,
                                    imageUrl = element.productItem.freeOngkirImageUrl
                            ),
                            labelGroupList = element.productItem.labelGroupList.map { recommendationLabel ->
                                ProductCardModel.LabelGroup(
                                        position = recommendationLabel.position,
                                        title = recommendationLabel.title,
                                        type = recommendationLabel.type
                                )
                            },
                            hasThreeDots = true
                    )
            )

            setImageProductViewHintListener(element.productItem, object: ViewHintListener {
                override fun onViewHint() {
                    if (element.productItem.isTopAds) {
                        context?.run {
                            TopAdsUrlHitter(className).hitImpressionUrl(
                                    this,
                                    element.productItem.trackerImageUrl,
                                    element.productItem.productId.toString(),
                                    element.productItem.name,
                                    element.productItem.imageUrl
                            )
                        }
                    }
                    element.listener.onProductImpression(element.productItem)
                }
            })

            setOnClickListener {
                element.listener.onProductClick(element.productItem, element.productItem.type, adapterPosition)
                if (element.productItem.isTopAds) {
                    context?.run {
                        TopAdsUrlHitter(className).hitClickUrl(
                                this,
                                element.productItem.clickUrl,
                                element.productItem.productId.toString(),
                                element.productItem.name,
                                element.productItem.imageUrl
                        )
                    }
                }
            }

            setThreeDotsOnClickListener {
                element.listener.onThreeDotsClick(element.productItem, adapterPosition)
            }
        }
    }

    override fun bind(element: ProductRecommendationViewModel, payloads: MutableList<Any>) {
        if (payloads.getOrNull(0) !is Boolean) return

        productCardView?.setThreeDotsOnClickListener {
            element.listener.onThreeDotsClick(element.productItem, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.viewmodel_product_recommendation_item
        private const val className: String = "com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder"
    }

}