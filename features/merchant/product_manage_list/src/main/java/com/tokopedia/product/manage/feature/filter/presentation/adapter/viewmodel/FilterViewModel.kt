package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapterTypeFactory

class FilterViewModel (
        val title: String,
        val names: List<String>
) : Visitable <FilterAdapterTypeFactory> {

    companion object {
        val LAYOUT = 0
    }

    override fun type(typeFactory: FilterAdapterTypeFactory?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}