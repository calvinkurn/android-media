package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

data class ProductItemDataView(
    /**
     * Mandatory params
     */
    val shop: Shop = Shop(),
    val position: Int = 0,
    val parentId: String = "",
    val sourceEngine: String = "",
    val boosterList: String = "",
    var productCardModel: ProductCardCompactUiModel,

    /**
     * Optional Params
     */
    val widgetTitle: String = "",
    val shopId: String = "",
    val shopName: String = "",
    val shopType: String = "",
    val categoryBreadcrumbs: String = "",
    val type: String = "",
) : Visitable<BaseSearchCategoryTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0

    data class Shop(
            val id: String = "",
            val name: String = "",
    )
}
