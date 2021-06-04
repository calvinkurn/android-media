package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoErrorStateUiModel

class PromoErrorViewHolder(private val view: View,
                           private val listener: PromoCheckoutActionListener)
    : AbstractViewHolder<PromoErrorStateUiModel>(view) {
    private val errorStatePromo by lazy {
        view.findViewById<EmptyStateUnify>(R.id.error_state_promo)
    }

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_error_promo_page
    }

    override fun bind(element: PromoErrorStateUiModel) {
        errorStatePromo.apply {
            setTitle(element.uiData.title)
            setImageUrl(element.uiData.imageUrl)
            setDescription(element.uiData.description)
            setPrimaryCTAText(element.uiData.buttonText)
            setPrimaryCTAClickListener {
                listener.onClickErrorStateButton()
            }
        }
    }
}