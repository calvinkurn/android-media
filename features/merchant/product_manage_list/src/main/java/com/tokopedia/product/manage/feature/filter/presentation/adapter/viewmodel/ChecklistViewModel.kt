package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory

class ChecklistViewModel(
        val name: String,
        val id: String,
        val isSelected: Boolean
) : Visitable<SelectAdapterTypeFactory> {

    override fun type(adapterTypeFactory: SelectAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

}