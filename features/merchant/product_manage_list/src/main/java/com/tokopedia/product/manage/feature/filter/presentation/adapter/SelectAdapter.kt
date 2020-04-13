package com.tokopedia.product.manage.feature.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.SelectDiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel

class SelectAdapter(
        adapterTypeFactory: SelectAdapterTypeFactory
) : BaseAdapter<SelectAdapterTypeFactory>(adapterTypeFactory) {

    fun updateSelectData(selectUiModels: List<SelectUiModel>) {
        visitables.clear()
        visitables.addAll(selectUiModels)
        notifyDataSetChanged()
    }

    fun updateChecklistData(checklistUiModels: List<ChecklistUiModel>) {
        val diffUtilCallback = SelectDiffUtil(visitables.filterIsInstance(ChecklistUiModel::class.java), checklistUiModels)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(checklistUiModels)
        result.dispatchUpdatesTo(this)
    }

    fun reset() {
        visitables.forEach {
            (it as? ChecklistUiModel)?.isSelected = false
        }
        notifyDataSetChanged()
    }
}