package com.tokopedia.promocheckout.list.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel

class PromoLastSeenAdapter(var listData: MutableList<PromoCheckoutLastSeenModel>,
                           val listenerLastSeen: PromoLastSeenViewHolder.ListenerLastSeen)
    : RecyclerView.Adapter<PromoLastSeenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoLastSeenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_promo_last_seen, parent, false)
        return PromoLastSeenViewHolder(view, listenerLastSeen)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: PromoLastSeenViewHolder, position: Int) {
        holder.bind(listData.get(position))
    }

}
