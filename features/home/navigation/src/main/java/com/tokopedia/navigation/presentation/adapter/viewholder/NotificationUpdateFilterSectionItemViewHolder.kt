package com.tokopedia.navigation.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel


/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFilterSectionItemViewHolder(
        itemView: View,
        val itemSectionListener: FilterSectionListener
) : AbstractViewHolder<NotificationUpdateFilterSectionItemViewModel>(itemView) {

    private val category: TextView = itemView.findViewById(R.id.chips_item)

    interface FilterSectionListener {
        fun onFilterClicked(element: NotificationUpdateFilterSectionItemViewModel)
    }

    override fun bind(element: NotificationUpdateFilterSectionItemViewModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.isEmpty()) return
        val payload = payloads[0]
        when (payload) {
            PAYLOAD_DESELECTED -> deselectChip(element)
            PAYLOAD_SELECTED -> selectChip(element)
        }
    }

    private fun selectChip(element: NotificationUpdateFilterSectionItemViewModel) {
        setChipBackground(element)
    }

    private fun deselectChip(element: NotificationUpdateFilterSectionItemViewModel) {
        element.selected = false
        setChipBackground(element)
    }

    private fun setChipBackground(element: NotificationUpdateFilterSectionItemViewModel) {
        if(element.selected) {
            category.background = MethodChecker.getDrawable(category.context, R.drawable.bg_item_filter_pressed)
        } else {
            category.background = MethodChecker.getDrawable(category.context, R.drawable.bg_filter_green_border)
        }
    }

    override fun bind(element: NotificationUpdateFilterSectionItemViewModel) {
        category.text = element.text
        setChipBackground(element)
        category.setOnClickListener {
            element.selected = !element.selected
            itemSectionListener.onFilterClicked(element)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_filter_item_section
        const val PAYLOAD_DESELECTED = "payload_deselected"
        const val PAYLOAD_SELECTED = "payload_selected"
    }

}