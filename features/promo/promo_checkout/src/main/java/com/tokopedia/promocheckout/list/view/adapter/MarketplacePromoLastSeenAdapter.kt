package com.tokopedia.promocheckout.list.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listpromolastseen.PromoHistoryItem

class MarketplacePromoLastSeenAdapter(var listData: MutableList<PromoHistoryItem>,
                                      private val listenerLastSeen: MarketplacePromoLastSeenViewHolder.ListenerLastSeen)
    : RecyclerView.Adapter<MarketplacePromoLastSeenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketplacePromoLastSeenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_last_seen_promo_history, parent, false)
        return MarketplacePromoLastSeenViewHolder(view, listenerLastSeen)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MarketplacePromoLastSeenViewHolder, position: Int) {
        holder.bind(listData[position])
    }

}
