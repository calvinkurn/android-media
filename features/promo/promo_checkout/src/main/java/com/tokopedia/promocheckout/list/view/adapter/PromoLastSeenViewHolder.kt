package com.tokopedia.promocheckout.list.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import kotlinx.android.synthetic.main.item_promo_last_seen.view.*

class PromoLastSeenViewHolder(val view : View, val listenerLastSeen: ListenerLastSeen) : RecyclerView.ViewHolder(view) {
    fun bind(data: PromoCheckoutLastSeenModel) {
        view.setOnClickListener { listenerLastSeen.onClickItemLastSeen(data) }
        view.voucherCode.text = data.promoCode
        view.descPromo.text = data.subtitle.toUpperCase()
    }

    interface ListenerLastSeen{
        fun onClickItemLastSeen(promoCheckoutLastSeenModelModel: PromoCheckoutLastSeenModel)
    }
}
