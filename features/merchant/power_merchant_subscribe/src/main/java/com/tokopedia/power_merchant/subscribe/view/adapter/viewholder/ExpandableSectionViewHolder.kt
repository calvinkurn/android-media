package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.ExpandableSectionUiModel
import kotlinx.android.synthetic.main.item_pm_expandable_widget_section.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableSectionViewHolder(itemView: View?) : AbstractViewHolder<ExpandableSectionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_expandable_widget_section
    }

    override fun bind(element: ExpandableSectionUiModel) {
        itemView.tvPmExpandableSection.text = element.text
    }
}