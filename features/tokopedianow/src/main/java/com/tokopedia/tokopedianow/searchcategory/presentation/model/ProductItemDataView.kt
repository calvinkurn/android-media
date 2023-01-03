package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

data class ProductItemDataView(
        val shop: Shop = Shop(),
        val position: Int = 0,
        val parentId: String = "",
        val sourceEngine: String = "",
        val boosterList: String = "",
        var productCardModel: TokoNowProductCardViewUiModel
): Visitable<BaseSearchCategoryTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0

    data class Shop(
            val id: String = "",
            val name: String = "",
    )
}
