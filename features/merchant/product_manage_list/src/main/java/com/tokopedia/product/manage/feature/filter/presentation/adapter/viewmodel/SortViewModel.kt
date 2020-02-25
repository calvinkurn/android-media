package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapterTypeFactory

class SortViewModel(
        val names: List<String>
) : Visitable<FilterAdapterTypeFactory> {

    override fun type(adapterTypeFactory: FilterAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

}