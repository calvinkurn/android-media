package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel.UiData.Companion.PROMO_TYPE_GLOBAL
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel.UiData.Companion.PROMO_TYPE_MERCHANT_OFFICIAL
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel.UiData.Companion.PROMO_TYPE_POWER_MERCHANT
import kotlinx.android.synthetic.main.item_promo_list_header.view.*

class PromoListHeaderViewHolder(private val view: View,
                                private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_list_header
    }

    override fun bind(element: PromoListHeaderUiModel) {
        if (element.uiData.promoType == PROMO_TYPE_GLOBAL) {
            itemView.image_promo_list_header.setImageResource(R.drawable.ic_promo_global)
        } else if (element.uiData.promoType == PROMO_TYPE_MERCHANT_OFFICIAL) {
            itemView.image_promo_list_header.setImageResource(R.drawable.ic_badge_shop_official)
        } else if (element.uiData.promoType == PROMO_TYPE_POWER_MERCHANT) {
            itemView.image_promo_list_header.setImageResource(R.drawable.ic_power_merchant)
        }
        itemView.label_promo_list_header_title.text = element.uiData.title
        itemView.label_promo_list_header_sub_title.text = element.uiData.subTitle
        if (!element.uiState.isCollapsed) {
            itemView.image_chevron.rotation = 180f
        } else {
            itemView.image_chevron.rotation = 0f
        }
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) listener.onClickPromoListHeader(adapterPosition)
        }
    }

}