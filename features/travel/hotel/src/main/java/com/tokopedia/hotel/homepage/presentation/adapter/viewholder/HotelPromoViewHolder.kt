package com.tokopedia.hotel.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelPromoBinding
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.hotel.homepage.presentation.adapter.HotelPromoAdapter
import com.tokopedia.media.loader.loadImageRounded

/**
 * @author by furqan on 10/04/19
 */
class HotelPromoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemHotelPromoBinding.bind(view)

    fun bind(element: HotelPromoEntity, listener: HotelPromoAdapter.PromoClickListener?, position: Int) {
        with(binding) {
            ivHotelPromo.loadImageRounded(element.attributes.imageUrl, ROUND_RADIUS)

            root.setOnClickListener {
                listener?.onPromoClicked(element, position)
                RouteManager.route(root.context, element.attributes.linkUrl)
            }
        }
    }

    companion object {
        const val ROUND_RADIUS = 8f

        val LAYOUT = R.layout.item_hotel_promo
    }
}
