package com.tokopedia.recommendation_widget_common.widget.productcard.common

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData

/**
 * @author by yoasfs on 09/06/20
 */

//listener for grid product card
interface RecomCommonProductCardListener {

    //product card click and impression
    fun onProductCardImpressed(data: RecommendationWidget, recomItem: RecommendationItem, position: Int)
    fun onProductCardClicked(data: RecommendationWidget, recomItem: RecommendationItem, position: Int, applink: String)

    /**
     * these method to use the ads byteio tracker
     */

    fun onAreaClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {}
    fun onProductImageClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {}
    fun onSellerInfoClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {}
    fun onViewAttachedToWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int)
    fun onViewDetachedFromWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int, visiblePercentage: Int)

    fun onRecomProductCardAddToCartNonVariant(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int, quantity: Int)
    fun onRecomProductCardAddVariantClick(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int)

    //for see more card click
    fun onSeeMoreCardClicked(data: RecommendationWidget, applink: String)

    //for banner card clicked
    fun onBannerCardClicked(data: RecommendationWidget, applink: String)
    fun onBannerCardImpressed(data: RecommendationWidget)
}
