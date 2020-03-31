package com.tokopedia.product.manage.feature.filter.presentation.adapter.factory

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel

interface SelectTypeFactory {
    fun type(selectUiModel: SelectUiModel): Int
    fun type(checklistUiModel: ChecklistUiModel): Int
}