package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel
import kotlinx.android.synthetic.main.paylater_see_more_item.view.*

class PayLaterSeeMoreViewHolder(itemView: View, private val interaction: PayLaterOptionInteraction) :
    AbstractViewHolder<SeeMoreOptionsUiModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_see_more_item
    }

    override fun bind(element: SeeMoreOptionsUiModel?) {
        itemView.seeMoreButton.text = "Lihat ${element?.remainingItems?.size?: 0} Lainnya"
        itemView.setOnClickListener {
            interaction.seeMoreOptions(adapterPosition)
        }
    }
}
