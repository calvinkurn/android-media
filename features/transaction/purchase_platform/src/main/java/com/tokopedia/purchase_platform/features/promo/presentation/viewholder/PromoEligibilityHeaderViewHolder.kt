package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoEligibilityHeaderUiModel
import kotlinx.android.synthetic.main.item_promo_eligibility_header.view.*

class PromoEligibilityHeaderViewHolder(private val view: View,
                                       private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEligibilityHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_eligibility_header
    }

    override fun bind(element: PromoEligibilityHeaderUiModel) {
        itemView.label_promo_eligibility_header_title.text = element.uiData.title

        if (element.uiState.isEnabled) {
            itemView.label_promo_eligibility_header_subtitle.text = element.uiData.subTitle
            itemView.label_promo_eligibility_header_subtitle.show()
        } else {
            itemView.label_promo_eligibility_header_subtitle.hide()
        }
    }

}