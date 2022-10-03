package com.tokopedia.logisticcart.schedule_slot.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.DividerType
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography

class ChooseTimeViewHolder(private val view: View, private val listener: ScheduleSlotListener) : AbstractViewHolder<ChooseTimeUiModel>(view) {

    private val title: Typography = view.findViewById(com.tokopedia.checkout.R.id.tv_title)
    private val description: Typography = view.findViewById(com.tokopedia.checkout.R.id.tv_description)
    private val divider: DividerUnify = view.findViewById(com.tokopedia.checkout.R.id.divider)
    private val iconCheck: IconUnify = view.findViewById(com.tokopedia.checkout.R.id.icon_check)

    override fun bind(element: ChooseTimeUiModel) {
        title.visibility = if (element.title.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        title.visibility = if (element.content.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        title.text = element.title
        description.text = element.content

        if (element.isEnabled) {
            description.setTextColor(view.context.resources.getColor(com.tokopedia.checkout.R.color.Unify_NN950))

            view.setOnClickListener {
                iconCheck.visibility = View.VISIBLE
                listener.onClickTimeListener(element)
            }
        } else {
            description.setTextColor(view.context.resources.getColor(com.tokopedia.checkout.R.color.Unify_NN950))
        }




        when (element.divider) {
            DividerType.THIN -> {
                divider.visibility = View.VISIBLE
                val params = divider.layoutParams
                params.height = 3
                divider.layoutParams = params
            }
            DividerType.THICK -> {
                divider.visibility = View.VISIBLE
                val params = divider.layoutParams
                params.height = 16
                divider.layoutParams = params            }
            else -> {
                divider.visibility = View.GONE
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.checkout.R.layout.viewholder_choose_time
    }
}
