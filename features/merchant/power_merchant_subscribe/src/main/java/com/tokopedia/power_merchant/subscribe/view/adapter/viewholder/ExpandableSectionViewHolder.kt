package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmExpandableWidgetSectionBinding
import com.tokopedia.power_merchant.subscribe.view.model.ExpandableSectionUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableSectionViewHolder(itemView: View?) : AbstractViewHolder<ExpandableSectionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_expandable_widget_section
    }

    private val binding: ItemPmExpandableWidgetSectionBinding? by viewBinding()

    override fun bind(element: ExpandableSectionUiModel) {
        binding?.run {
            tvPmExpandableSection.text = element.text
            horLinePmSeparator.isVisible = element.shouldShowTopSeparator
        }
    }
}