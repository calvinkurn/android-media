package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SectionTitleUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel
import kotlinx.android.synthetic.main.paylater_section_header_item.view.*

class PayLaterSectionHeaderViewHolder(itemView: View) :
    AbstractViewHolder<SectionTitleUiModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_section_header_item
    }

    override fun bind(element: SectionTitleUiModel?) {
        if (element?.titleText.isNullOrEmpty())
            itemView.tvSectionTitle.gone()
        else {
            itemView.tvSectionTitle.visible()
            itemView.tvSectionTitle.text = element?.titleText.orEmpty()
        }
    }
}
