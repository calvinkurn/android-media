package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter.Companion.CHECKLIST_FILTER_PAYLOAD
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistWidget
import kotlinx.android.synthetic.main.item_checklist.view.*
import kotlinx.android.synthetic.main.widget_checklist.view.*

class CheckListViewHolder(view: View, private val checklistClickListener: ChecklistClickListener) : AbstractViewHolder<ChecklistUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_checklist
    }

    private var checklistWidget: ChecklistWidget = itemView.checklistWidget

    override fun bind(element: ChecklistUiModel) {
        itemView.setOnClickListener {
            checklistClickListener.onChecklistClick(element)
            checklistWidget.checklist.isChecked = !checklistWidget.checklist.isChecked
        }
        checklistWidget.bind(element, checklistClickListener)
    }

    override fun bind(element: ChecklistUiModel?, payloads: MutableList<Any>) {
        if(element == null || payloads.isNullOrEmpty()) return

        when (payloads.getOrNull(0) as? Int) {
            CHECKLIST_FILTER_PAYLOAD -> {
                checklistWidget.updateChecklist(element, checklistClickListener)
            }
        }
    }
}