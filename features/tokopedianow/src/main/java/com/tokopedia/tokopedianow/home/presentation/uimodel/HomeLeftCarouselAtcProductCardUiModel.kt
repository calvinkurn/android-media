package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel.HomeLeftCarouselAtcProductCardTypeFactory

data class HomeLeftCarouselAtcProductCardUiModel(
    var id: String? = null,
    var brandId: String = "",
    var categoryId: String = "",
    var parentProductId: String = "",
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
    val categoryBreadcrumbs: String = "",
    val productCardModel: TokoNowProductCardViewUiModel,
    val position: Int = 0
): Visitable<HomeLeftCarouselAtcProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: HomeLeftCarouselAtcProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
