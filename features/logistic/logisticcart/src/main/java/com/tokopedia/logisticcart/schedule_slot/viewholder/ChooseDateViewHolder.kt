package com.tokopedia.logisticcart.schedule_slot.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.logisticcart.schedule_slot.adapter.ChooseDateAdapter
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.unifyprinciples.Typography

class ChooseDateViewHolder(private val view: View, private val listener: ScheduleSlotListener) : AbstractViewHolder<ChooseDateUiModel>(view) {

    private val title: Typography = view.findViewById(com.tokopedia.checkout.R.id.tv_title)
    private val recyclerView: RecyclerView = view.findViewById(com.tokopedia.checkout.R.id.rv_choose_date)
    override fun bind(element: ChooseDateUiModel) {
        title.text = element.title
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ChooseDateAdapter(element.content, listener)
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.checkout.R.layout.viewholder_choose_date
    }
}
