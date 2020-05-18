package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel

class RecommendationItemViewHolder (
        itemView: View,
        val listener: RecommendationListener
) : AbstractViewHolder<RecommendationItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_recommendation_card_small_grid
    }

    private val productCardView: ProductCardGridView? by lazy{
        itemView.findViewById<ProductCardGridView>(R.id.productCardView)
    }

    override fun bind(recommendationItemViewModel: RecommendationItemViewModel) {
        val view = productCardView ?: return
        val recommendationItem = recommendationItemViewModel.recommendationItem
        view.setProductModel(
                ProductCardModel(
                        slashedPrice = recommendationItem.slashedPrice,
                        productName = recommendationItem.name,
                        formattedPrice = recommendationItem.price,
                        productImageUrl = recommendationItem.imageUrl,
                        isTopAds = recommendationItem.isTopAds,
                        discountPercentage = recommendationItem.discountPercentage.toString(),
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
                        },
                        hasThreeDots = true
                )
        )

        view.setOnClickListener {
            listener.onProductClick(recommendationItem, "", adapterPosition)
        }

        view.setImageProductViewHintListener(recommendationItemViewModel, createImageProductViewHintListener(recommendationItemViewModel))

        view.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(recommendationItemViewModel.recommendationItem, adapterPosition)
        }
    }

    private fun createImageProductViewHintListener(recommendationItemViewModel: RecommendationItemViewModel): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                listener.onProductImpression(recommendationItemViewModel.recommendationItem)
            }
        }
    }

    override fun bind(recommendationItemViewModel: RecommendationItemViewModel, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return

        productCardView?.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(recommendationItemViewModel.recommendationItem, adapterPosition)
        }
    }
}