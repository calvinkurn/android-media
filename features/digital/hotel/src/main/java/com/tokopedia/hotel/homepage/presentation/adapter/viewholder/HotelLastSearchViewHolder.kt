package com.tokopedia.hotel.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R

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

    companion object {
        const val ROUND_RADIUS = 8f

        val LAYOUT = R.layout.item_hotel_homepage_last_search
    }
}