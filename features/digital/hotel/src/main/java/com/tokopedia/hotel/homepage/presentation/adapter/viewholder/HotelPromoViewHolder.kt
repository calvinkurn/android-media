package com.tokopedia.hotel.homepage.presentation.adapter.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
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

    fun bind(element: HotelPromoEntity, listener: HotelPromoAdapter.PromoClickListener?, position: Int) {
        with(itemView) {

            // banner height 1/3 of phone width
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)

            val layoutParams = iv_hotel_promo.layoutParams
            layoutParams.height = metrics.widthPixels / 3
            iv_hotel_promo.layoutParams = layoutParams

            ImageHandler.loadImageWithoutPlaceholder(this.iv_hotel_promo, element.attributes.imageUrl)

            this.setOnClickListener {
                listener?.onPromoClicked(element, position)
                RouteManager.route(context, element.attributes.linkUrl)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_promo
    }
}