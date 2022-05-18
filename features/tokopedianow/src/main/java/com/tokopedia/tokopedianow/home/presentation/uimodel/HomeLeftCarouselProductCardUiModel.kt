package com.tokopedia.tokopedianow.home.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselProductCardTypeFactory

data class HomeLeftCarouselProductCardUiModel(
    var id: String? = null,
    var rvState: Parcelable? = null,
    val productCardModel: ProductCardModel
): Visitable<HomeLeftCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: HomeLeftCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
