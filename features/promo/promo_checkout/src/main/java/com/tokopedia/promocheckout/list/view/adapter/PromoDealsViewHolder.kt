package com.tokopedia.promocheckout.list.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import kotlinx.android.synthetic.main.item_promo_last_seen.view.*

class PromoDealsViewHolder(val view : View, private val listenerDealsPromoCode: ListenerDealsPromoCode) : RecyclerView.ViewHolder(view) {
    fun bind(data: TravelCollectiveBanner.Banner) {
        view.setOnClickListener { listenerDealsPromoCode.onClickItemPromo(data) }

        if (data.attributes.promoCode.isNotEmpty()) {
            view.voucherCode.text = data.attributes.promoCode
            view.descPromo.text = data.attributes.description.toUpperCase()
        }


        if (data.attributes.promoCode.isEmpty()) {
            view.layoutPromoLastSeen.visibility = View.GONE
        } else {
            view.layoutPromoLastSeen.visibility = View.VISIBLE
        }
    }

    interface ListenerDealsPromoCode{
        fun onClickItemPromo(promoCheckoutDealsPromoCode: TravelCollectiveBanner.Banner)
    }
}
