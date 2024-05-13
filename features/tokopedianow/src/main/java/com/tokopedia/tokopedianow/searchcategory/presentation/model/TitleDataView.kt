package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

data class TitleDataView(
        val titleType: TitleType,
        val hasSeeAllCategoryButton: Boolean = false,
        val chooseAddressData: LocalCacheModel?
): Visitable<BaseSearchCategoryTypeFactory> {
    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this).orZero()
}

sealed class TitleType
data class CategoryTitle(val categoryName: String): TitleType()
object SearchTitle : TitleType()
object AllProductTitle : TitleType()
