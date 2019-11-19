package com.tokopedia.promocheckout.list.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.promocheckout.list.model.listpromolastseen.PromoHistoryItem
import kotlinx.android.synthetic.main.item_last_seen_promo_history.view.*

import kotlinx.android.synthetic.main.item_promo_last_seen.view.layoutPromoLastSeen

class PromoLastSeenViewHolder(val view: View, val listenerLastSeen: ListenerLastSeen) : RecyclerView.ViewHolder(view) {
    fun bind(data: PromoHistoryItem) {
        view.tvPromoCode.text = data.promoCode
        view.tvTitleLastCard.text = data.promoContent?.description
        view.btnGunakanLast.setOnClickListener { listenerLastSeen.onClickItemLastSeen(data) }
        if (data.promoCode?.isEmpty()!!) {
            view.ticketPromoHistory.visibility = View.GONE
        } else {
            view.ticketPromoHistory.visibility = View.VISIBLE
        }
    }

    interface ListenerLastSeen {
        fun onClickItemLastSeen(ppromoHistoryItem: PromoHistoryItem)
    }
}
