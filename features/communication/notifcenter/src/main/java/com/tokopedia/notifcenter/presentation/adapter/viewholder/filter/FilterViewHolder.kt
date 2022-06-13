package com.tokopedia.notifcenter.presentation.adapter.viewholder.filter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.filter.FilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class FilterViewHolder(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<FilterUiModel>(itemView) {

    private val filter: ChipsUnify? = itemView?.findViewById(R.id.chips_filter)


    interface Listener {
        fun onFilterClicked(element: FilterUiModel)
        fun isSelected(element: FilterUiModel): Boolean
    }

    override fun bind(element: FilterUiModel?, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        toggleChipState()
    }

    override fun bind(element: FilterUiModel) {
        bindFilterText(element)
        bindClick(element)
        bindState(element)
    }

    private fun bindFilterText(element: FilterUiModel) {
        filter?.chipText = element.tagName
    }

    private fun bindClick(element: FilterUiModel) {
        filter?.setOnClickListener {
            listener?.onFilterClicked(element)
            toggleChipState()
        }
    }

    private fun bindState(element: FilterUiModel) {
        val isSelected = listener?.isSelected(element) ?: false
        val state = if (isSelected) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
        filter?.chipType = state
    }

    private fun toggleChipState() {
        val state = when (filter?.chipType) {
            ChipsUnify.TYPE_NORMAL -> ChipsUnify.TYPE_SELECTED
            ChipsUnify.TYPE_SELECTED -> ChipsUnify.TYPE_NORMAL
            else -> null
        }
        state?.let {
            filter?.chipType = it
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notification_filter_v2
    }
}