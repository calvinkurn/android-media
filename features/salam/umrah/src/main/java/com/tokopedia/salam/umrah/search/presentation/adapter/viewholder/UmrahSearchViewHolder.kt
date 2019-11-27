package com.tokopedia.salam.umrah.search.presentation.adapter.viewholder

import android.content.res.Resources
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.UmrahHotelRating.getAllHotelRatings
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil.getSlashedPrice
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import kotlinx.android.synthetic.main.item_umrah_search.view.*


/**
 * @author by furqan on 18/10/2019
 */
class UmrahSearchViewHolder(view: View) : AbstractViewHolder<UmrahSearchProduct>(view) {

    override fun bind(element: UmrahSearchProduct) {
        val hotelsRating = getAllHotelRatings(element.hotels)
        with(itemView) {
            iv_umrah_image.loadImage(element.banners.first())
            label_umrah_duration.text = getString(R.string.umrah_search_duration_days, element.durationDays.toString())
            tg_umrah_title.text = element.title
            tg_umrah_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.originalPrice, false)
            tg_umrah_calendar.text = getFormattedCalendarString(resources, element.departureDate, element.returningDate)
            tg_umrah_hotel.text = resources.getString(R.string.umrah_search_hotel_rating_x, hotelsRating)
            tg_umrah_plane.text = element.airlines.first().name
            tg_umrah_start_from_label.text = getSlashedPrice(resources, element.slashPrice)
        }
    }

    private fun getFormattedCalendarString(resources: Resources, departureDate: String, returningDate: String): String {
        return resources.getString(R.string.umrah_pdp_calendar,
                TravelDateUtil.dateToString("dd MMM", TravelDateUtil
                        .stringToDate(TravelDateUtil.YYYY_MM_DD, departureDate)),
                TravelDateUtil.dateToString("dd MMM yyyy", TravelDateUtil
                        .stringToDate(TravelDateUtil.YYYY_MM_DD, returningDate)))
    }

    companion object {
        val LAYOUT = R.layout.item_umrah_search
    }
}