package com.tokopedia.tokopedianow.search.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory

data class BroadMatchDataView(
    val seeMoreModel: ProductCardCompactCarouselSeeMoreUiModel,
    val headerModel: TokoNowDynamicHeaderUiModel,
    val broadMatchItemModelList: List<ProductCardCompactCarouselItemUiModel> = listOf(),
): Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory?) =
        typeFactory?.type(this) ?: 0

}
