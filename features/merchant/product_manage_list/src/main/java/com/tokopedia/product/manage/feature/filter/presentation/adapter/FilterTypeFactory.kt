package com.tokopedia.product.manage.feature.filter.presentation.adapter

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*

interface FilterTypeFactory {
    fun type(sortViewModel: SortViewModel): Int

    fun type(categoriesViewModel: CategoriesViewModel): Int

    fun type(otherFilterViewModel: OtherFilterViewModel): Int

    fun type(etalaseViewModel: EtalaseViewModel): Int

    fun type(headerViewModel: HeaderViewModel): Int

    fun type(seeAllViewModel: SeeAllViewModel): Int

    fun type(filterViewModel: FilterViewModel): Int
}