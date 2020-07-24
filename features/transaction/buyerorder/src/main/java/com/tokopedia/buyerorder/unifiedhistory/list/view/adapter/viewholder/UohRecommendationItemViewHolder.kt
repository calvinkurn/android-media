package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohRecommendationItemViewHolder(itemView: View) : UohItemAdapter.BaseViewHolder<RecommendationItem>(itemView) {
    private val productCardView: ProductCardGridView by lazy { itemView.findViewById<ProductCardGridView>(R.id.uoh_product_item) }

    override fun bind(element: RecommendationItem, position: Int) {
        productCardView.run {
            setProductModel(
                ProductCardModel(
                    slashedPrice = element.slashedPrice,
                    productName = element.name,
                    formattedPrice = element.price,
                    productImageUrl = element.imageUrl,
                    isTopAds = element.isTopAds,
                    discountPercentage = element.discountPercentage.toString(),
                    reviewCount = element.countReview,
                    ratingCount = element.rating,
                    shopLocation = element.location,
                    shopBadgeList = element.badgesUrl.map {
                        ProductCardModel.ShopBadge(imageUrl = it
                                ?: "")
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = element.isFreeOngkirActive,
                            imageUrl = element.freeOngkirImageUrl
                    ),
                    labelGroupList = element.labelGroupList.map {
                        ProductCardModel.LabelGroup(
                                title = it.title,
                                position = it.position,
                                type = it.type
                        )
                    }
                )
            )
        }
    }
}