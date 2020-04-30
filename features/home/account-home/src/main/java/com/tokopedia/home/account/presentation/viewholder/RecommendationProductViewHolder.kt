package com.tokopedia.home.account.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel

/**
 * @author devarafikry on 24/07/19.
 */
class RecommendationProductViewHolder(itemView: View, val accountItemListener: AccountItemListener) : AbstractViewHolder<RecommendationProductViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_account_product_recommendation
    }
    private val productCardView: ProductCardGridView by lazy { itemView.findViewById<ProductCardGridView>(R.id.account_product_recommendation) }

    override fun bind(element: RecommendationProductViewModel) {
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.product.slashedPrice,
                            productName = element.product.name,
                            formattedPrice = element.product.price,
                            productImageUrl = element.product.imageUrl,
                            isTopAds = element.product.isTopAds,
                            discountPercentage = element.product.discountPercentage.toString(),
                            reviewCount = element.product.countReview,
                            ratingCount = element.product.rating,
                            shopLocation = element.product.location,
                            isWishlistVisible = true,
                            isWishlisted = element.product.isWishlist,
                            shopBadgeList = element.product.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it
                                        ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.product.isFreeOngkirActive,
                                    imageUrl = element.product.freeOngkirImageUrl
                            ),
                            labelGroupList = element.product.labelGroupList.map { recommendationLabel ->
                                ProductCardModel.LabelGroup(
                                        position = recommendationLabel.position,
                                        title = recommendationLabel.title,
                                        type = recommendationLabel.type
                                )
                            },
                            hasThreeDots = true
                    )
            )
            setImageProductViewHintListener(element.product, object : ViewHintListener {
                override fun onViewHint() {
                    accountItemListener.onProductRecommendationImpression(element.product, adapterPosition)
                }
            })

            setOnClickListener {
                accountItemListener.onProductRecommendationClicked(element.product, adapterPosition, element.widgetTitle)
            }

            setThreeDotsOnClickListener {
                accountItemListener.onProductRecommendationThreeDotsClicked(element.product, adapterPosition)
            }
        }
    }
}
