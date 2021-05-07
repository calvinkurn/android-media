package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryTypeFactory

data class ProductItemDataView(
        val id: String = "",
        val imageUrl300: String = "",
        val name: String = "",
        val price: String = "",
        val priceInt: Double = 0.0,
): Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}