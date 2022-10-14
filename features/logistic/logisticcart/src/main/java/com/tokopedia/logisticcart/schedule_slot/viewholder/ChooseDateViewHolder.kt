package com.tokopedia.logisticcart.schedule_slot.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.databinding.ViewholderChooseDateBinding
import com.tokopedia.logisticcart.schedule_slot.adapter.ChooseDateAdapter
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener

class ChooseDateViewHolder(
    private val viewBinding: ViewholderChooseDateBinding,
    private val listener: ScheduleSlotListener
) : AbstractViewHolder<ChooseDateUiModel>(viewBinding.root) {

    override fun bind(element: ChooseDateUiModel) {
        viewBinding.tvTitle.run {
            if (element.title.isNotEmpty()) {
                text = element.title
                visible()
            } else {
                gone()
            }
        }
        viewBinding.rvChooseDate.run {
            layoutManager =
                LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChooseDateAdapter(element.content, listener)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.logisticcart.R.layout.viewholder_choose_date
    }
}
