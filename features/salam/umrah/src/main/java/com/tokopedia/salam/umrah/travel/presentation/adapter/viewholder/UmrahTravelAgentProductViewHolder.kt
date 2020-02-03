package com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil
import kotlinx.android.synthetic.main.item_umrah_search.view.*

/**
 * @author by Firman on 3/2/20
 */

class UmrahTravelAgentProductViewHolder (view: View): AbstractViewHolder<UmrahProductModel.UmrahProduct>(view){
    override fun bind(element: UmrahProductModel.UmrahProduct) {
        with(itemView){
            iv_umrah_image.loadImage(element.banners.first())
            label_umrah_duration.text = resources.getString(R.string.umrah_search_duration_days, element.durationDays.toString())
            tg_umrah_title.text = element.title
            tg_umrah_price.text = CurrencyFormatter.getRupiahFormat(element.originalPrice)
            tg_umrah_calendar.text = element.ui.travelDates
            tg_umrah_hotel.text = element.ui.hotelStars
            tg_umrah_plane.text = element.airlines.first().name
            tg_umrah_start_from_label.text = UmrahPriceUtil.getSlashedPrice(resources, element.slashPrice)

            setOnClickListener {
                RouteManager.route(context, ApplinkConst.SALAM_UMRAH_PDP, element.slugName)
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.item_umrah_search
    }
}