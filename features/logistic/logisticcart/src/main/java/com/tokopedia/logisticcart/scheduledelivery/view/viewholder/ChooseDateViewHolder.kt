package com.tokopedia.logisticcart.scheduledelivery.view.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.databinding.ViewholderChooseDateBinding
import com.tokopedia.logisticcart.scheduledelivery.utils.ScheduleSlotListener
import com.tokopedia.logisticcart.scheduledelivery.view.adapter.ChooseDateAdapter
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseDateUiModel

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
            val chooseDateAdapter = ChooseDateAdapter(element.content, listener)
            layoutManager =
                LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = chooseDateAdapter
            val scrollTo = chooseDateAdapter.selectedDatePosition()
            if (scrollTo != -1) {
                smoothScrollToPosition(scrollTo)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.logisticcart.R.layout.viewholder_choose_date
    }
}
