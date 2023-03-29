package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeProductCarouselChipsUiModel(
    val id: String,
    val header: TokoNowDynamicHeaderUiModel?,
    val chipList: List<TokoNowChipUiModel>,
    val selectedChip: TokoNowChipUiModel,
    val carouselItemList: List<Visitable<*>>,
    val state: TokoNowProductRecommendationState = TokoNowProductRecommendationState.LOADING
): HomeLayoutUiModel(id) {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
