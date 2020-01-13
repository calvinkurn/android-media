package com.tokopedia.hotel.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.CardUnify

/**
 * @author by furqan on 10/04/19
 */
class HotelLastSearchViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

//    fun bind(element: HotelPromoEntity, listener: HotelPromoAdapter.PromoClickListener?, position: Int) {
//        with(itemView) {
//            this.iv_hotel_promo.loadImageRounded(element.attributes.imageUrl, ROUND_RADIUS)
//
//            this.setOnClickListener {
//                listener?.onPromoClicked(element, position)
//                RouteManager.route(context, element.attributes.linkUrl)
//            }
//        }
//    }

    val container = view.findViewById<CardUnify>(R.id.item_hotel_last_search_container)

    fun bind() {
        container.layoutParams.height = container.layoutParams.width * 2 / 3
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_homepage_last_search
    }
}