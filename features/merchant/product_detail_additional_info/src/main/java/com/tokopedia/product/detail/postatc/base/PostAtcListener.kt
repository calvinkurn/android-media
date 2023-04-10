package com.tokopedia.product.detail.postatc.base

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface PostAtcListener {
    fun impressComponent(componentTrackData: ComponentTrackData)

    fun onClickLihatKeranjang(cartId: String, componentTrackData: ComponentTrackData)

    fun goToAppLink(appLink: String)
    fun goToProduct(productId: String)
    fun refreshPage()

    fun fetchRecommendation(pageName: String, uniqueId: Int)
    fun onClickRecommendationItem(recommendationItem: RecommendationItem)
    fun onImpressRecommendationItem(recommendationItem: RecommendationItem)
}
