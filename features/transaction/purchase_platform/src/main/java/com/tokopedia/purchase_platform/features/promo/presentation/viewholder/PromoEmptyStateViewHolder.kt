package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoEmptyStateUiModel

class PromoEmptyStateViewHolder(private val view: View,
                                private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEmptyStateUiModel>(view) {

    private val emptyStatePromo by lazy {
        view.findViewById<EmptyStateUnify>(R.id.empty_state_promo)
    }

    companion object {
        val LAYOUT = R.layout.item_promo_empty
    }

    override fun bind(element: PromoEmptyStateUiModel) {
        emptyStatePromo.setTitle(element.uiData.title)
        emptyStatePromo.setDescription(element.uiData.description)
        emptyStatePromo.setImageUrl(element.uiData.imageUrl)

        if (element.uiState.isShowButton) {
            emptyStatePromo.setPrimaryCTAText(element.uiData.buttonText)
            emptyStatePromo.setPrimaryCTAClickListener {
                listener.onClickEmptyStateButton(element)
            }
        } else {
            emptyStatePromo.setPrimaryCTAText("")
        }
    }

}