package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

data class TitleDataView(
        val titleType: TitleType,
        val hasSeeAllCategoryButton: Boolean = false,
): Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}

sealed class TitleType
data class CategoryTitle(val categoryName: String): TitleType()
object SearchTitle : TitleType()
object AllProductTitle : TitleType()
