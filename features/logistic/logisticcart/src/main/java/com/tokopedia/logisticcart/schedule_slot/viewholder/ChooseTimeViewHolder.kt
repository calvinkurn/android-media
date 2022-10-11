package com.tokopedia.logisticcart.schedule_slot.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.DividerType
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ChooseTimeViewHolder(private val view: View, private val listener: ScheduleSlotListener) : AbstractViewHolder<ChooseTimeUiModel>(view) {

    private val title: Typography = view.findViewById(com.tokopedia.logisticcart.R.id.tv_title)
    private val description: Typography = view.findViewById(com.tokopedia.logisticcart.R.id.tv_description)
    private val note: Typography = view.findViewById(com.tokopedia.logisticcart.R.id.tv_note)
    private val divider: DividerUnify = view.findViewById(com.tokopedia.logisticcart.R.id.divider)
    private val iconCheck: IconUnify = view.findViewById(com.tokopedia.logisticcart.R.id.icon_check)

    override fun bind(element: ChooseTimeUiModel) {
        setView(element)
        bindValue(element)
        setState(element)
        setDivider(element.divider)
    }

    private fun setView(element: ChooseTimeUiModel) {
        /* set visibility */
        title.visibility = if (element.title.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        description.visibility = if (element.content.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        note.visibility = if (element.note.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        /* set dynamic margin */
        if (element.note.isEmpty()) {
            val params = description.layoutParams as ViewGroup.MarginLayoutParams
            description.layoutParams = params.apply {
                topMargin = 22.toPx()
            }
        } else {
            val params = description.layoutParams as ViewGroup.MarginLayoutParams
            description.layoutParams = params.apply {
                topMargin = 12.toPx()
            }
        }
    }

    private fun bindValue(element: ChooseTimeUiModel) {
        title.text = element.title
        description.text = HtmlLinkHelper(itemView.context, element.content).spannedString
        note.text = element.note
    }

    /** set state of view either enabled or disabled
     * if enabled, set click listener
     * otherwise, disable click
     */
    private fun setState(element: ChooseTimeUiModel) {
        if (element.isEnabled) {
            description.setTextColor(ContextCompat.getColor(view.context, com.tokopedia.logisticcart.R.color.Unify_NN950))

            view.setOnClickListener {
                iconCheck.visibility = View.VISIBLE
                listener.onClickTimeListener(element)
            }
        } else {
            description.setTextColor(ContextCompat.getColor(view.context, com.tokopedia.logisticcart.R.color.Unify_NN400))
        }
    }

    private fun setDivider(dividerType: DividerType) {
        when (dividerType) {
            DividerType.THIN -> {
                divider.visibility = View.VISIBLE
                val params = divider.layoutParams
                params.height = 3
                (params as ViewGroup.MarginLayoutParams).setMargins(16.toPx(), params.topMargin, 16.toPx(), params.bottomMargin)
                divider.layoutParams = params
            }
            DividerType.THICK -> {
                divider.visibility = View.VISIBLE
                val params = divider.layoutParams
                params.height = 16
                (params as ViewGroup.MarginLayoutParams).setMargins(0, params.topMargin, 0, params.bottomMargin)
                divider.layoutParams = params
            }
            else -> {
                divider.visibility = View.GONE
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.logisticcart.R.layout.viewholder_choose_time
    }
}
