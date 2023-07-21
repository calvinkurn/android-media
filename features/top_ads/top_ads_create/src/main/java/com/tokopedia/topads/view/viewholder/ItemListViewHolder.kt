package com.tokopedia.topads.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.create.databinding.ViewholderItemListBinding
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.topads.view.utils.ScheduleSlotListener
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ItemListViewHolder(
    private val viewBinding: ViewholderItemListBinding,
    private val listener: ScheduleSlotListener
) : AbstractViewHolder<ItemListUiModel>(viewBinding.root) {

    override fun bind(element: ItemListUiModel) {
        setView(element)
        bindValue(element)
        setSelected(element)
    }

    private fun setSelected(element: ItemListUiModel) {
        viewBinding.radioButton.isChecked = element.isSelected
    }

    private fun setView(element: ItemListUiModel) {
        /* set visibility */
        viewBinding.tvTitle.visibility = if (element.title.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        viewBinding.tvDescription.visibility = if (element.content.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        viewBinding.container.setOnClickListener {
            if(!element.isSelected) {
                listener.onClickItemListener(element.title)
                viewBinding.radioButton.isChecked = true
            }
        }
    }

    private fun bindValue(element: ItemListUiModel) {
        viewBinding.tvTitle.text = element.title
        viewBinding.tvDescription.text =
            HtmlLinkHelper(itemView.context, element.content).spannedString
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.topads.create.R.layout.topads_insight_centre_insight_selection_item

    }
}
