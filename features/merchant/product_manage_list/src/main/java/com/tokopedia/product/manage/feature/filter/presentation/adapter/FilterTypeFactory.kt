package com.tokopedia.product.manage.feature.filter.presentation.adapter

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*

interface FilterTypeFactory {
    fun type(filterViewModel: FilterViewModel): Int
}