package com.tokopedia.purchase_platform.features.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.common.view.widget.ViewUtils
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.holder_item_cart_new.view.*
import kotlinx.android.synthetic.main.item_promo_revamp.view.*

/**
 * Created by fwidjaja on 2020-02-26.
 */
class PromoRevampViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val ITEM_VIEW_PROMO_REVAMP = R.layout.item_promo_revamp
    }

    fun bindViewHolder() {
        itemView.cl_promo_funnel.background = ViewUtils.generateBackgroundWithShadow(itemView.cl_promo_funnel)
    }
}