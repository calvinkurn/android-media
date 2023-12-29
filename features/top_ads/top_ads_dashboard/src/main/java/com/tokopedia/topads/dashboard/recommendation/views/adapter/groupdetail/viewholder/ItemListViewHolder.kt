package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.databinding.TopadsInsightCentreInsightSelectionItemBinding
import com.tokopedia.topads.dashboard.recommendation.common.OnItemSelectChangeListener
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ListBottomSheetItemUiModel

class ItemListViewHolder(
    private val viewBinding: TopadsInsightCentreInsightSelectionItemBinding,
    private val listener: OnItemSelectChangeListener
) : AbstractViewHolder<ListBottomSheetItemUiModel>(viewBinding.root) {

    override fun bind(element: ListBottomSheetItemUiModel) {
        setView(element)
        bindValue(element)
        setSelected(element)
    }

    private fun setSelected(element: ListBottomSheetItemUiModel) {
        viewBinding.radioButton.isChecked = element.isSelected
    }

    private fun setView(element: ListBottomSheetItemUiModel) {
        viewBinding.tvTitle.showWithCondition(element.title.isNotEmpty())

        viewBinding.container.setOnClickListener {
            if(!element.isSelected) {
                listener.onClickItemListener(element.adType, element.groupId, element.title)
                viewBinding.radioButton.isChecked = true
            }
        }
    }

    private fun bindValue(element: ListBottomSheetItemUiModel) {
        viewBinding.tvTitle.text = element.title
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.topads.dashboard.R.layout.topads_insight_centre_insight_selection_item

    }
}
