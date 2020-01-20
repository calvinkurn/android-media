package com.tokopedia.purchase_platform.features.checkout.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.NotEligiblePromoHolderdata
import kotlinx.android.synthetic.main.item_promo_not_eligible.view.*

/**
 * Created by Irfan Khoirul on 2019-06-21.
 */

class PromoNotEligibleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_not_eligible
    }

    fun bind(model: NotEligiblePromoHolderdata) {
        itemView.tv_number.text = (adapterPosition + 1).toString()
        itemView.tv_promo_name.text = model.promoTitle
    }

}