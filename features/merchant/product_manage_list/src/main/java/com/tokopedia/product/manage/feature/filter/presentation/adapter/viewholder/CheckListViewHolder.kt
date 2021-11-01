package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemChecklistBinding
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter.Companion.CHECKLIST_FILTER_PAYLOAD
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.utils.view.binding.viewBinding

class CheckListViewHolder(view: View, private val checklistClickListener: ChecklistClickListener) : AbstractViewHolder<ChecklistUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_checklist
    }

    private val binding by viewBinding<ItemChecklistBinding>()

    override fun bind(element: ChecklistUiModel) {
        itemView.setOnClickListener {
            checklistClickListener.onChecklistClick(element)
            binding?.checklistWidget?.checklist?.isChecked =
                binding?.checklistWidget?.checklist?.isChecked == false
        }
        binding?.checklistWidget?.bind(element, checklistClickListener)
    }

    override fun bind(element: ChecklistUiModel?, payloads: MutableList<Any>) {
        if(element == null || payloads.isNullOrEmpty()) return

        when (payloads.getOrNull(0) as? Int) {
            CHECKLIST_FILTER_PAYLOAD -> {
                binding?.checklistWidget?.updateChecklist(element, checklistClickListener)
            }
        }
    }
}