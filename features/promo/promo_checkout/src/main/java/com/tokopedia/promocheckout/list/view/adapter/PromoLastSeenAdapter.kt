package com.tokopedia.promocheckout.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel

class PromoLastSeenAdapter(var listData: MutableList<PromoCheckoutLastSeenModel>,
                           private val listenerLastSeen: PromoLastSeenViewHolder.ListenerLastSeen,
                           var isDeals: Boolean)
    : RecyclerView.Adapter<PromoLastSeenViewHolder>() {

    constructor(listData: MutableList<PromoCheckoutLastSeenModel>, listenerLastSeen: PromoLastSeenViewHolder.ListenerLastSeen) :
            this(listData, listenerLastSeen, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoLastSeenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_promo_last_seen, parent, false)
        return PromoLastSeenViewHolder(view, listenerLastSeen)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: PromoLastSeenViewHolder, position: Int) {
        holder.bind(listData[position], isDeals)
    }

}