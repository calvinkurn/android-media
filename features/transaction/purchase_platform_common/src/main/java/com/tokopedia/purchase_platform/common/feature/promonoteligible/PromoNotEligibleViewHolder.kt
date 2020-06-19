package com.tokopedia.purchase_platform.common.feature.promonoteligible

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.R
import kotlinx.android.synthetic.main.item_promo_red_state.view.*

/**
 * Created by Irfan Khoirul on 2019-06-21.
 */

class PromoNotEligibleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_red_state
    }

    fun bind(model: NotEligiblePromoHolderdata) {
        if (model.showShopSection) {
            when {
                model.iconType == NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL -> itemView.image_merchant.setImageResource(R.drawable.ic_promo_global)
                model.iconType == NotEligiblePromoHolderdata.TYPE_ICON_OFFICIAL_STORE -> itemView.image_merchant.setImageResource(R.drawable.ic_badge_shop_official)
                model.iconType == NotEligiblePromoHolderdata.TYPE_ICON_POWER_MERCHANT -> itemView.image_merchant.setImageResource(R.drawable.ic_power_merchant)
                else -> itemView.image_merchant.gone()
            }
        } else {
            itemView.image_merchant.gone()
        }
        itemView.label_merchant.text = model.shopName
        if (model.promoTitle.isNotBlank()) {
            itemView.label_promo_title.text = model.promoTitle
            itemView.label_promo_title.show()
        } else {
            itemView.label_promo_title.gone()
        }

        if (model.errorMessage.isNotBlank()) {
            itemView.label_promo_error_message.text = model.errorMessage
            itemView.label_promo_error_message.show()
        } else {
            itemView.label_promo_error_message.gone()
        }
    }

}