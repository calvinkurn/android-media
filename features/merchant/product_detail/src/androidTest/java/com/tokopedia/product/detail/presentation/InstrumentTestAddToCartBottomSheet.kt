package com.tokopedia.product.detail.presentation

import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class InstrumentTestAddToCartBottomSheet(
        private val instrumentTestTopAdsCounter: InstrumentTestTopAdsCounter
) : AddToCartDoneBottomSheet() {

    companion object {
        private const val className: String = "com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet"
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        if (item.isTopAds) {
            context?.run {
                TopAdsUrlHitter(className).hitClickUrl(
                        this,
                        item.clickUrl,
                        item.productId.toString(),
                        item.name,
                        item.imageUrl
                )
                instrumentTestTopAdsCounter.onTopAdsUrlHit()
            }
        }
    }
}