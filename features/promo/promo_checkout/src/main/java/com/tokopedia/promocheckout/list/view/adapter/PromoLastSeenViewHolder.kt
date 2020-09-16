package com.tokopedia.promocheckout.list.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import kotlinx.android.synthetic.main.item_promo_last_seen.view.*

class PromoLastSeenViewHolder(val view : View, private val listenerLastSeen: ListenerLastSeen) : RecyclerView.ViewHolder(view) {
    fun bind(data: PromoCheckoutLastSeenModel) {
        view.setOnClickListener { listenerLastSeen.onClickItemLastSeen(data) }
        view.voucherCode.text = data.promoCode
        view.descPromo.text = data.title.toUpperCase()

        if (data.promoCode.isEmpty()) {
            view.layoutPromoLastSeen.visibility = View.GONE
        } else {
            view.layoutPromoLastSeen.visibility = View.VISIBLE
        }
    }

    fun bindDeals(data: TravelCollectiveBanner.Banner) {
        view.setOnClickListener { listenerLastSeen.onClickItemPromo(data) }

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

    interface ListenerLastSeen{
        fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel)
        fun onClickItemPromo(promoCheckoutDealsPromoCode: TravelCollectiveBanner.Banner)
    }
}
