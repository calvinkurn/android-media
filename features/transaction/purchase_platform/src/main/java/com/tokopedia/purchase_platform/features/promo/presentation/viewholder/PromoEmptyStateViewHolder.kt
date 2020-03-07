package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoEmptyStateUiModel
import kotlinx.android.synthetic.main.item_promo_empty.view.*

class PromoEmptyStateViewHolder(private val view: View,
                                private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEmptyStateUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_empty
    }

    override fun bind(element: PromoEmptyStateUiModel) {
        itemView.empty_state_promo.setTitle(element.uiData.title)
        itemView.empty_state_promo.setDescription(element.uiData.description)
        itemView.empty_state_promo.setImageUrl(element.uiData.imageUrl)

        if (element.uiState.isShowButton) {
            itemView.empty_state_promo.setPrimaryCTAText(element.uiData.buttonText)
            itemView.empty_state_promo.setPrimaryCTAClickListener {
                listener.onClickEmptyStateButton(element)
            }
        } else {
            itemView.empty_state_promo.setPrimaryCTAText("")
        }
    }

}