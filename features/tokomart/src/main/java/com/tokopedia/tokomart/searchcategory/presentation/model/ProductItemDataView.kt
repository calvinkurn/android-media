package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

data class ProductItemDataView(
        val id: String = "",
        val imageUrl300: String = "",
        val name: String = "",
        val price: String = "",
        val priceInt: Double = 0.0,
        val discountPercentage: Int = 0,
        val originalPrice: String = "",
        val parentId: String = "",
        val shop: Shop = Shop(),
        val ratingAverage: String = "",
        val variantATC: VariantATCDataView? = null,
        val nonVariantATC: NonVariantATCDataView? = null,
        val labelGroupDataViewList: List<LabelGroupDataView> = listOf(),
        val labelGroupVariantDataViewList: List<LabelGroupVariantDataView> = listOf(),
): Visitable<BaseSearchCategoryTypeFactory> {

    val discountPercentageString
        get() = if (discountPercentage > 0) "$discountPercentage%" else ""

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0

    data class Shop(
            val id: String = "",
    )
}