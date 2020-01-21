package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import kotlinx.android.synthetic.main.seller_home_card_widget.view.*

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class CardViewHolder(itemView: View?) : AbstractViewHolder<CardWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.seller_home_card_widget
    }

    override fun bind(element: CardWidgetUiModel) {
        with(itemView) {
            tvCardHello.text = element.title
        }
    }
}