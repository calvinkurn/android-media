package com.tokopedia.purchase_platform.features.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.promocheckout.common.view.widget.ViewUtils
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.item_promo_checkout.view.*

/**
 * Created by fwidjaja on 2020-02-26.
 */
class PromoCheckoutViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val ITEM_VIEW_PROMO_CHECKOUT = R.layout.item_promo_checkout
    }

    fun bindViewHolder() {
        itemView.cl_promo_funnel.background = ViewUtils.generateBackgroundWithShadow(itemView.cl_promo_funnel)
        itemView.item_divider.gone()
    }
}