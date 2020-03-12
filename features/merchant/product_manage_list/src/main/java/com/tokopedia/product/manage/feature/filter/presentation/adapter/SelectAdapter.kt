package com.tokopedia.product.manage.feature.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil.SelectDiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel

class SelectAdapter(
        adapterTypeFactory: SelectAdapterTypeFactory
) : BaseAdapter<SelectAdapterTypeFactory>(adapterTypeFactory) {

    fun updateSelectData(selectViewModels: List<SelectViewModel>) {
        visitables.clear()
        visitables.addAll(selectViewModels)
        notifyDataSetChanged()
    }

    fun updateChecklistData(checklistViewModels: List<ChecklistViewModel>) {
        val diffUtilCallback = SelectDiffUtil(visitables as List<ChecklistViewModel>, checklistViewModels)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(checklistViewModels)
        result.dispatchUpdatesTo(this)
    }

    fun reset() {
        visitables.forEachIndexed { index, visitable ->
            (visitable as? ChecklistViewModel)?.isSelected = false
            notifyItemChanged(index)
        }
    }
}