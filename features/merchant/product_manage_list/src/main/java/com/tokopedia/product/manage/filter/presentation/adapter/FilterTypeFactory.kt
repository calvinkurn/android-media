package com.tokopedia.product.manage.filter.presentation.adapter

import com.tokopedia.product.manage.filter.presentation.adapter.viewmodel.SortViewModel

interface FilterTypeFactory {
    fun type(sortViewModel: SortViewModel): Int
}