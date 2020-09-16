package com.tokopedia.promocheckout.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel

class PromoLastSeenAdapter(var listData: MutableList<PromoCheckoutLastSeenModel>,
                           var listDataDeals: MutableList<TravelCollectiveBanner.Banner>,
                           private val listenerLastSeen: PromoLastSeenViewHolder.ListenerLastSeen)
    : RecyclerView.Adapter<PromoLastSeenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoLastSeenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_promo_last_seen, parent, false)
        return PromoLastSeenViewHolder(view, listenerLastSeen)
    }

    override fun getItemCount(): Int {
        if (listData.isNotEmpty()) {
            return listData.size
        } else {
            return listDataDeals.size
        }
    }

    override fun onBindViewHolder(holder: PromoLastSeenViewHolder, position: Int) {
        if (listData.isNotEmpty()) {
            holder.bind(listData[position])
        } else if (listDataDeals.isNotEmpty()) {
         holder.bindDeals(listDataDeals[position])
        }
    }

}