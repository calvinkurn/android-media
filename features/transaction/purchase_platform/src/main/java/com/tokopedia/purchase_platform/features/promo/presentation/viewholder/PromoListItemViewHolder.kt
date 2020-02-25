package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListItemUiModel
import kotlinx.android.synthetic.main.item_promo_list_item.view.*

class PromoListItemViewHolder(private val view: View,
                              private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListItemUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_list_item
    }

    override fun bind(element: PromoListItemUiModel) {
        itemView.counter.text = element.uiData.promoId.toString()
        itemView.card_promo_item.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) listener.onClickPromoListItem(position)
        }
        if (element.uiState.isSellected) {
            itemView.card_promo_item.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green_200))
        } else {
            itemView.card_promo_item.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.grey_500))
        }
    }

}