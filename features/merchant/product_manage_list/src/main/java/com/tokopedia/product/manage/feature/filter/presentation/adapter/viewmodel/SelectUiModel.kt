package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory

class SelectUiModel(
        val id: String = "",
        val name: String = "",
        var value: String = "",
        var isSelected: Boolean = false
) : Visitable<SelectAdapterTypeFactory> {

    override fun type(adapterTypeFactory: SelectAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

}