package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.FilterAdapterTypeFactory

class FilterUiModel (
        val title: String,
        val data: MutableList<FilterDataUiModel>,
        var isChipsShown: Boolean
) : Visitable <FilterAdapterTypeFactory> {

    override fun type(typeFactory: FilterAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}