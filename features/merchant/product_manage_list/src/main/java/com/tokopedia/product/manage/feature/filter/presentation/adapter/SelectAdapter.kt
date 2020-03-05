package com.tokopedia.product.manage.feature.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.CheckListDiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.SelectDiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel

class SelectAdapter(
        adapterTypeFactory: SelectAdapterTypeFactory
) : BaseAdapter<SelectAdapterTypeFactory>(adapterTypeFactory) {

    fun updateSelectData(selectViewModels: List<SelectViewModel>) {
        val diffResult = DiffUtil.calculateDiff(
                SelectDiffUtil(visitables as List<SelectViewModel>, selectViewModels))
        visitables.clear()
        visitables.addAll(selectViewModels)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateChecklistData(checklistViewModels: List<ChecklistViewModel>) {
        val diffResult = DiffUtil.calculateDiff(
                CheckListDiffUtil(visitables as List<ChecklistViewModel>, checklistViewModels))
        visitables.clear()
        visitables.addAll(checklistViewModels)
        diffResult.dispatchUpdatesTo(this)
    }
}