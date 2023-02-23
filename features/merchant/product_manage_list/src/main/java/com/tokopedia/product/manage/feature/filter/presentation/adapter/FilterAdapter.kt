package com.tokopedia.product.manage.feature.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*

class FilterAdapter(
        adapterTypeFactory: FilterAdapterTypeFactory
) : BaseAdapter<FilterAdapterTypeFactory>(adapterTypeFactory) {

    fun updateData(filterUiModels: List<FilterUiModel>) {
        visitables.clear()
        visitables.addAll(filterUiModels)
        notifyItemRangeChanged(0,visitables.size)
    }
}
