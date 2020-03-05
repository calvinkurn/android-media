package com.tokopedia.product.manage.feature.filter.presentation.adapter.factory

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel

interface SelectTypeFactory {
    fun type(selectViewModel: SelectViewModel): Int
    fun type(checklistViewModel: ChecklistViewModel): Int
}