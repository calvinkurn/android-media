package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.Content
import com.tokopedia.pdpsimulation.paylater.domain.model.SectionTitleUiModel

class InstallmentDividerViewHolder(itemView: View) :
    AbstractViewHolder<Content>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_installment_divider_item
    }

    override fun bind(element: Content?) {}
}
