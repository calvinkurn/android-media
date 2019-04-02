package com.tokopedia.promocheckout.list.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel

class PromoLastSeenViewHolder(val view : View, val listenerLastSeen: ListenerLastSeen) : RecyclerView.ViewHolder(view) {
    fun bind(data: PromoCheckoutLastSeenModel) {
        view.setOnClickListener { listenerLastSeen.onClickItemLastSeen(data) }
    }

    interface ListenerLastSeen{
        fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel)
    }
}
