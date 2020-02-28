package com.tokopedia.product.manage.feature.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
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
        visitables.clear()
        visitables.addAll(checklistViewModels)
        notifyDataSetChanged()
    }

    fun clearAllChecklists() {
        for(visitable in visitables) {
            if(visitable is ChecklistViewModel) {
                visitable.isSelected = false
            }
        }
        notifyDataSetChanged()
    }
}