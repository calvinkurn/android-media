package com.tokopedia.product.manage.feature.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.FilterDiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.SelectDiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*

class FilterAdapter(
        adapterTypeFactory: FilterAdapterTypeFactory
) : BaseAdapter<FilterAdapterTypeFactory>(adapterTypeFactory) {

    fun updateData(filterUiModels: List<FilterUiModel>) {
        val diffUtilCallback = FilterDiffUtil(visitables.filterIsInstance<FilterUiModel>(), filterUiModels)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(filterUiModels)
        result.dispatchUpdatesTo(this)
    }
}