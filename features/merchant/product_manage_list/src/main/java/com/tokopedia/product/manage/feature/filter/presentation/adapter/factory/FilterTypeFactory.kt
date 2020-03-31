package com.tokopedia.product.manage.feature.filter.presentation.adapter.factory

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*

interface FilterTypeFactory {
    fun type(filterUiModel: FilterUiModel): Int
}