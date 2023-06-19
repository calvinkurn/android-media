package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.databinding.TopadsInsightCentreInsightSelctionItemBinding
import com.tokopedia.topads.dashboard.recommendation.utils.OnItemSelectChangeListener
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ItemListUiModel

class ItemListViewHolder(
    private val viewBinding: TopadsInsightCentreInsightSelctionItemBinding,
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
        viewBinding.tvTitle.showWithCondition(element.title.isNotEmpty())

        viewBinding.container.setOnClickListener {
            if(!element.isSelected) {
                listener.onClickItemListener(element.adType, element.groupId, element.title)
                viewBinding.radioButton.isChecked = true
            }
        }
    }

    private fun bindValue(element: ItemListUiModel) {
        viewBinding.tvTitle.text = element.title
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = com.tokopedia.topads.dashboard.R.layout.topads_insight_centre_insight_selction_item

    }
}
