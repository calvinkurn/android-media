package com.tokopedia.tokopedianow.search.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory

data class BroadMatchDataView(
    val seeMoreModel: TokoNowSeeMoreCardCarouselUiModel,
    val headerModel: TokoNowDynamicHeaderUiModel,
    val broadMatchItemModelList: List<TokoNowProductCardCarouselItemUiModel> = listOf(),
): Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory?) =
        typeFactory?.type(this) ?: 0

}
