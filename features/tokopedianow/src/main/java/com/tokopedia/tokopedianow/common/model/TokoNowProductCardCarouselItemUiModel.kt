package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductCardCarouselTypeFactory

data class TokoNowProductCardCarouselItemUiModel(
    /**
     * Optional params
     */
    val recomType: String = "",
    val pageName: String = "",
    val shopId: String = "",
    val shopName: String = "",
    val shopType: String = "",
    val isTopAds: Boolean = false,
    val appLink: String = "",
    val parentId: String = "0",
    val alternativeKeyword: String = "",

    /**
     * Mandatory params
     */
    val id: String,
    var productCardModel: TokoNowProductCardViewUiModel,
): Visitable<TokoNowProductCardCarouselTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: TokoNowProductCardCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}
