package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapterTypeFactory

class FilterViewModel (
        val title: String,
        val names: List<String>,
        val id: List<String>,
        val values: List<String> = listOf()
) : Visitable <FilterAdapterTypeFactory> {

    override fun type(typeFactory: FilterAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}