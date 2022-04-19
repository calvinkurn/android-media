package com.tokopedia.product.manage.feature.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.ChecklistDiffUtil
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
        val diffUtilCallback = ChecklistDiffUtil(visitables.filterIsInstance(ChecklistUiModel::class.java), checklistUiModels)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(checklistUiModels)
        result.dispatchUpdatesTo(this)
    }

    fun reset() {
        visitables.filterIsInstance<ChecklistUiModel>().mapIndexed { index, checkList ->
            if(checkList.isSelected) {
                checkList.isSelected = false
                notifyItemChanged(index, CHECKLIST_FILTER_PAYLOAD)
            }
        }
    }

    companion object {
        const val CHECKLIST_FILTER_PAYLOAD = 808
    }
}