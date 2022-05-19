package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselProductCardTypeFactory

data class HomeLeftCarouselProductCardUiModel(
    var id: String? = null,
    var brandId: String = "",
    var categoryId: String = "",
    var parentProductId: String = "0",
    var shopId: String = "0",
    var shopName: String = "",
    var appLink: String = "",
    var channelId: String = "",
    var channelHeaderName: String = "",
    var channelPageName: String = "",
    var channelType: String = "",
    var recommendationType: String = "",
    var warehouseId: String = "",
    var campaignCode: String = "",
    val productCardModel: ProductCardModel
): Visitable<HomeLeftCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: HomeLeftCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
