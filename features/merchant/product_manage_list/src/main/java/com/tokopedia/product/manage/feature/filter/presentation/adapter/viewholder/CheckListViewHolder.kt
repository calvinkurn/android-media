package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistWidget
import kotlinx.android.synthetic.main.widget_checklist.view.*

class CheckListViewHolder(view: View, private val checklistClickListener: ChecklistClickListener) : AbstractViewHolder<ChecklistViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_checklist
    }

    private var checklistWidget: ChecklistWidget = itemView.findViewById(R.id.checklist_widget)

    override fun bind(element: ChecklistViewModel) {
        checklistWidget.bind(element, checklistClickListener)
    }
}