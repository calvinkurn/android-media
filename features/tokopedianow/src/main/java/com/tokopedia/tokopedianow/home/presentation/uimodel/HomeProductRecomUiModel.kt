package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeProductRecomUiModel(
    val id: String,
    val title: String,
    val productList: List<TokoNowProductCardCarouselItemUiModel>,
    val seeMoreModel: TokoNowSeeMoreCardCarouselUiModel? = null,
    val headerModel: TokoNowDynamicHeaderUiModel? = null
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
