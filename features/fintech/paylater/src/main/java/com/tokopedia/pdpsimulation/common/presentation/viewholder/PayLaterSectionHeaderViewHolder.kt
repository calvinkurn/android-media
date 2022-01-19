package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SectionTitleUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel

class PayLaterSectionHeaderViewHolder(itemView: View) :
    AbstractViewHolder<SectionTitleUiModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_section_header_item
    }

    override fun bind(element: SectionTitleUiModel?) {}
}
