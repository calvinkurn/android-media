package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokomart.home.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderBannerUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderProductPersonalizedUiModel
import com.tokopedia.unifycomponents.UnifyButton

class HomeSliderProductPersonalizedViewHolder(
        itemView: View
): AbstractViewHolder<HomeSliderProductPersonalizedUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_slider_product_personalized
    }

    private val recommendationCard = itemView.findViewById<ProductCardListView>(R.id.productCardView)

    override fun bind(element: HomeSliderProductPersonalizedUiModel?) {
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