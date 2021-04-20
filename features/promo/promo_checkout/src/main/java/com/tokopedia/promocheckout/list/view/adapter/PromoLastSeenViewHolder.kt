package com.tokopedia.promocheckout.list.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import kotlinx.android.synthetic.main.item_promo_last_seen.view.*

class PromoLastSeenViewHolder(val view: View, private val listenerLastSeen: ListenerLastSeen) : RecyclerView.ViewHolder(view) {

    fun bind(data: PromoCheckoutLastSeenModel, isDeals: Boolean = false) {
        view.setOnClickListener { listenerLastSeen.onClickItemLastSeen(data) }
        view.voucherCode.text = data.promoCode
        if (isDeals == true) {
            view.descPromoDeals.visibility = View.VISIBLE
            view.descPromoDeals.text = data.title
            view.descPromo.visibility = View.GONE
        } else {
            view.descPromo.visibility = View.VISIBLE
            view.descPromo.text = data.title.toUpperCase()
            view.descPromoDeals.visibility = View.GONE
        }

        if (data.promoCode.isEmpty()) {
            view.layoutPromoLastSeen.visibility = View.GONE
        } else {
            view.layoutPromoLastSeen.visibility = View.VISIBLE
        }
    }

    interface ListenerLastSeen {
        fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel)
    }
}
