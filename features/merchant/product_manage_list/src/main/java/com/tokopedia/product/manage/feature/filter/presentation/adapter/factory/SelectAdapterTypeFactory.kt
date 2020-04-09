package com.tokopedia.product.manage.feature.filter.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.CheckListViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.SelectViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener

class SelectAdapterTypeFactory(
        private val selectClickListener: SelectClickListener,
        private val checklistClickListener: ChecklistClickListener
) : BaseAdapterTypeFactory(), SelectTypeFactory {
    override fun type(selectUiModel: SelectUiModel): Int {
        return SelectViewHolder.LAYOUT
    }

    override fun type(checklistUiModel: ChecklistUiModel): Int {
        return CheckListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SelectViewHolder.LAYOUT -> SelectViewHolder(parent, selectClickListener)
            CheckListViewHolder.LAYOUT -> CheckListViewHolder(parent, checklistClickListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}