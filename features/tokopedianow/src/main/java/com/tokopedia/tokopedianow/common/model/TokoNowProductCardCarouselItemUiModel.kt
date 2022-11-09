package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductCardCarouselTypeFactory

data class TokoNowProductCardCarouselItemUiModel(
    val id: String,
    val recomType: String,
    val pageName: String,
    val productCardModel: TokoNowProductCardViewUiModel,
    val shopId: String,
    val shopName: String,
    val shopType: String,
    val isTopAds: Boolean,
    val appLink: String,
    val parentId: String
): Visitable<TokoNowProductCardCarouselTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: TokoNowProductCardCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}
