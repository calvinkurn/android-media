package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.Content
import com.tokopedia.pdpsimulation.paylater.domain.model.SectionTitleUiModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.paylater_installment_info_item.view.*

class PayLaterInstallmentInfoViewHolder(itemView: View) :
    AbstractViewHolder<Content>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_installment_info_item
    }

    override fun bind(element: Content) {
        itemView.tvTitle.text = element.title?.parseAsHtml()
        itemView.tvValue.text = element.value?.parseAsHtml()
        if (element.type == 4) {
            itemView.tvTitle.setType(Typography.BODY_3)
            itemView.tvValue.gone()
        }
    }
}
