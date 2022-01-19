package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction

class PayLaterDetailViewHolder(itemView: View, interaction: PayLaterOptionInteraction) :
    AbstractViewHolder<Detail>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_partner_card_item
    }

    override fun bind(element: Detail?) {}
}
