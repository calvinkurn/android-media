package com.tokopedia.tokopedianow.home.presentation.uimodel

import android.os.Parcelable
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
    var rvState: Parcelable? = null,
    var appLink: String = "",
    var channelId: String = "",
    var channelHeaderName: String = "",
    var channelPageName: String = "",
    var channelType: String = "",
    val productCardModel: ProductCardModel
): Visitable<HomeLeftCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: HomeLeftCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
