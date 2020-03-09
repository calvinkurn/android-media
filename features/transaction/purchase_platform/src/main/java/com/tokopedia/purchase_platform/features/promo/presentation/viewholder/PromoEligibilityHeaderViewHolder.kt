package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
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
        if (element.uiData.subTitle.isNotBlank()) {
            itemView.label_promo_eligibility_header_subtitle.text = element.uiData.subTitle
            itemView.label_promo_eligibility_header_subtitle.show()
        } else {
            itemView.label_promo_eligibility_header_subtitle.hide()
        }

        if (element.uiState.isEnabled) {
            itemView.image_chevron.gone()
            itemView.setOnClickListener { }
        } else {
            if (!element.uiState.isCollapsed) {
                itemView.image_chevron.rotation = 180f
            } else {
                itemView.image_chevron.rotation = 0f
            }
            itemView.image_chevron.show()
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) listener.onClickPromoEligibilityHeader(element)
            }
        }
    }

}