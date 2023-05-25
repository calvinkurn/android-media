package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.databinding.ViewholderItemListBinding
import com.tokopedia.topads.dashboard.recommendation.utils.OnItemSelectChangeListener
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ItemListUiModel

class ItemListViewHolder(
    private val viewBinding: ViewholderItemListBinding,
    private val listener: OnItemSelectChangeListener
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

        viewBinding.container.setOnClickListener {
            if(!element.isSelected) {
                listener.onClickItemListener(element.adType, element.groupId)
                viewBinding.radioButton.isChecked = true
            }
        }
    }

    private fun bindValue(element: ItemListUiModel) {
        viewBinding.tvTitle.text = element.title
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.topads.dashboard.R.layout.viewholder_item_list

    }
}
