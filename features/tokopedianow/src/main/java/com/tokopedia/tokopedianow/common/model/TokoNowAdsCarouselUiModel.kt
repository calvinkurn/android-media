package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowAdsCarouselTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowAdsCarouselUiModel(
    val id: String,
    val items: List<ProductCardCompactCarouselItemUiModel>,
    @TokoNowLayoutState val state: Int
): Visitable<TokoNowAdsCarouselTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: TokoNowAdsCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getChangePayload(newItem: TokoNowAdsCarouselUiModel): Any? {
        return when {
            items != newItem.items ||
            state != newItem.state -> true
            else -> null
        }
    }
}
