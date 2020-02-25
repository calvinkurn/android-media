package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoEligibleHeaderUiModel
import kotlinx.android.synthetic.main.item_promo_eligible_header.view.*

class PromoEligibleHeaderViewHolder(private val view: View,
                                    private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEligibleHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_eligible_header
    }

    override fun bind(element: PromoEligibleHeaderUiModel) {
        itemView.label_promo_eligible_header_title.text = element.uiData.title
        itemView.label_promo_eligible_header_subtitle.text = element.uiData.subTitle
    }

}