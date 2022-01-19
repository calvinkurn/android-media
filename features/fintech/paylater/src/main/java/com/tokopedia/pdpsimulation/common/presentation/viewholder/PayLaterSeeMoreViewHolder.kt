package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel

class PayLaterSeeMoreViewHolder(itemView: View, val interaction: PayLaterOptionInteraction) :
    AbstractViewHolder<SeeMoreOptionsUiModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_see_more_item
    }

    override fun bind(element: SeeMoreOptionsUiModel?) {}
}
