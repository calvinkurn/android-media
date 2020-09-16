package com.tokopedia.promocheckout.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner

class PromoDealsAdapter(var listData: MutableList<TravelCollectiveBanner.Banner>,
                        private val listenerLastSeen: PromoDealsViewHolder.ListenerDealsPromoCode)
    : RecyclerView.Adapter<PromoDealsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoDealsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_promo_last_seen, parent, false)
        return PromoDealsViewHolder(view, listenerLastSeen)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: PromoDealsViewHolder, position: Int) {
        holder.bind(listData[position])
    }
}