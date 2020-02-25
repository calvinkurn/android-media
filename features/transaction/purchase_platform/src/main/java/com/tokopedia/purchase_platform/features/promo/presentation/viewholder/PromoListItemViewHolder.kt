package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListItemUiModel

class PromoListItemViewHolder(private val view: View,
                              private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListItemUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_list_item
    }

    override fun bind(element: PromoListItemUiModel) {

    }

}