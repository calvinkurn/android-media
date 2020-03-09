package com.tokopedia.purchase_platform.features.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata
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
            if (model.shopIconUrl.isNotBlank()) {
                ImageHandler.loadImageRounded2(itemView.context, itemView.image_merchant, model.shopIconUrl)
                itemView.image_merchant.show()
            } else {
                itemView.image_merchant.gone()
            }
        } else {
            itemView.image_merchant.gone()
        }
        itemView.label_merchant.text = model.shopName
        itemView.label_promo_title.text = model.promoTitle
        itemView.label_promo_error_message.text = model.errorMessage
    }

}