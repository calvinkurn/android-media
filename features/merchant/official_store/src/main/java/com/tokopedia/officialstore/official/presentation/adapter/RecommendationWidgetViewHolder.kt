package com.tokopedia.officialstore.official.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.officialstore.R
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifycomponents.UnifyButton

class RecommendationWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val productCard: ProductCardGridView = itemView.findViewById(R.id.recomWidgetItem)

    companion object {
        val LAYOUT = R.layout.recom_widget_item
    }

    fun bind(element: RecommendationItem) {
        productCard.setProductModel(
            ProductCardModel(
                slashedPrice = element.slashedPrice,
                productName = element.name,
                formattedPrice = element.price,
                productImageUrl = element.imageUrl,
                isTopAds = element.isTopAds,
                discountPercentage = element.discountPercentage,
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
                labelGroupList = element.labelGroupList.map { recommendationLabel ->
                    ProductCardModel.LabelGroup(
                        position = recommendationLabel.position,
                        title = recommendationLabel.title,
                        type = recommendationLabel.type,
                        imageUrl = recommendationLabel.imageUrl
                    )
                },
                hasAddToCartButton = true,
                addToCartButtonType = UnifyButton.Type.MAIN
            )
        )
    }

}