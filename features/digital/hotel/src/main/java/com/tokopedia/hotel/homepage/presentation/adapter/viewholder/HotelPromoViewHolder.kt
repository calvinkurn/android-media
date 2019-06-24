package com.tokopedia.hotel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.hotel.homepage.presentation.adapter.HotelPromoAdapter
import kotlinx.android.synthetic.main.item_hotel_promo.view.*

/**
 * @author by furqan on 10/04/19
 */
class HotelPromoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(element: HotelPromoEntity, listener: HotelPromoAdapter.PromoClickListener?) {
        with(itemView) {
            ImageHandler.loadImageWithoutPlaceholder(this.iv_hotel_promo, element.attributes.imageUrl)

            this.setOnClickListener {
                listener?.onPromoClicked(element)
                RouteManager.route(context, element.attributes.linkUrl)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_promo
    }
}