package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoIneligibleHeaderUiModel

class PromoIneligibleHeaderViewHolder(private val view: View,
                                      private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoIneligibleHeaderUiModel>(view) {

    companion object {
        val LAYOUT = 5
    }

    override fun bind(element: PromoIneligibleHeaderUiModel) {

    }

}