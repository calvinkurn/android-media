package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoTabUiModel

class PromoTabViewHolder(private val view: View,
                         private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoTabUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_tab
    }

    override fun bind(element: PromoTabUiModel) {

    }

}