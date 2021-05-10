package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderProductPersonalizedUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx

class HomeProductPersonalizedViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_product_peronalized
    }

    private val recommendationCard = itemView.findViewById<ProductCardListView>(R.id.productCardView)

    fun bind(model: HomeSliderProductPersonalizedUiModel) {
        if (adapterPosition == 0) {
            itemView.setMargin(16.toPx(),0,0,0)
        }
        recommendationCard.applyCarousel()
        recommendationCard.setProductModel(
                ProductCardModel(
                        productImageUrl = "https://cdn4.iconfinder.com/data/icons/small-n-flat/24/user-alt-512.png",
                        productName = "tesing",
                        discountPercentage = "20%",
                        slashedPrice = "1000",
                        formattedPrice = "1000",
                        hasAddToCartButton = true,
                        isTopAds = true,
                        isOutOfStock = false,
                        ratingCount = 100,
                        reviewCount = 120,
                        countSoldRating = "200",
                        shopLocation = "netherland",
                        addToCartButtonType = UnifyButton.Type.MAIN,
//                        shopBadgeList = recommendation.grid.badges.map {
//                            ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
//                        }
                )
        )
        val addToCartButton = recommendationCard.findViewById<UnifyButton>(R.id.buttonAddToCart)
        addToCartButton.text = "Buy it again"
        recommendationCard.setAddToCartOnClickListener {}
        itemView.setOnClickListener {}
    }
}