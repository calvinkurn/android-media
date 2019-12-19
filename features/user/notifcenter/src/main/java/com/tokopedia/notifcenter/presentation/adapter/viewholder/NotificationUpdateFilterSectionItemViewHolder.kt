package com.tokopedia.notifcenter.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterSectionViewBean


/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFilterSectionItemViewHolder(
        itemView: View,
        private val itemSectionListener: FilterSectionListener
) : AbstractViewHolder<NotificationUpdateFilterSectionViewBean>(itemView) {

    private val category: TextView = itemView.findViewById(R.id.chips_item)

    interface FilterSectionListener {
        fun onFilterClicked(element: NotificationUpdateFilterSectionViewBean)
    }

    override fun bind(element: NotificationUpdateFilterSectionViewBean?, payloads: MutableList<Any>) {
        if (element == null || payloads.isEmpty()) return
        val payload = payloads[0]
        when (payload) {
            PAYLOAD_DESELECTED -> deselectChip(element)
            PAYLOAD_SELECTED -> selectChip(element)
        }
    }

    private fun selectChip(element: NotificationUpdateFilterSectionViewBean) {
        setChipBackground(element)
    }

    private fun deselectChip(element: NotificationUpdateFilterSectionViewBean) {
        element.selected = false
        setChipBackground(element)
    }

    private fun setChipBackground(element: NotificationUpdateFilterSectionViewBean) {
        if(element.selected) {
            category.background = MethodChecker.getDrawable(category.context, R.drawable.bg_item_filter_pressed)
        } else {
            category.background = MethodChecker.getDrawable(category.context, R.drawable.bg_filter_green_border)
        }
    }

    override fun bind(element: NotificationUpdateFilterSectionViewBean) {
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