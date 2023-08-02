package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.utils.Util
import com.tokopedia.pdpsimulation.paylater.domain.model.Content
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.android.synthetic.main.paylater_installment_info_item.view.*

class PayLaterInstallmentInfoViewHolder(itemView: View) :
    AbstractViewHolder<Content>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_installment_info_item
    }

    override fun bind(element: Content) {
        itemView.tvTitle.text = Util.getTextRBPRemoteConfig(
            itemView.context,
            element.title?.parseAsHtml(),
            if (itemView.context.isDarkMode()) element.titleFormattedDark.parseAsHtml()
            else element.titleFormattedLight.parseAsHtml()
        )

        itemView.tvValue.text = element.value?.parseAsHtml()
        if (element.type == 4) {
            itemView.tvTitle.setType(Typography.BODY_3)
            itemView.tvValue.gone()
        }
    }
}
