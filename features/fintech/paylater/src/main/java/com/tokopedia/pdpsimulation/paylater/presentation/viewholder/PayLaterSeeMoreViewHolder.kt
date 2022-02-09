package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
        itemView.seeMoreButton.text = itemView.context.getString(R.string.paylater_see_more_offers, element?.remainingItems?.size?: 0)
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION)
                interaction.seeMoreOptions(adapterPosition)
        }
    }
}
