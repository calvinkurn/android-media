package com.tokopedia.salam.umrah.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil.getSlashedPrice
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import kotlinx.android.synthetic.main.item_umrah_search.view.*

/**
 * @author by furqan on 18/10/2019
 */
class UmrahSearchViewHolder(view: View) : AbstractViewHolder<UmrahSearchProduct>(view) {
    override fun bind(element: UmrahSearchProduct) {
        with(itemView) {
            iv_umrah_image.loadImage(element.banners.first())
            label_umrah_duration.text = getString(R.string.umrah_search_duration_days, element.durationDays.toString())
            tg_umrah_title.text = element.title
            tg_umrah_price.text = getRupiahFormat(element.originalPrice)
            tg_umrah_calendar.text = element.ui.travelDates
            tg_umrah_hotel.text = element.ui.hotelStars
            tg_umrah_plane.text = element.airlines.first().name
            tg_umrah_start_from_label.text = getSlashedPrice(resources, element.slashPrice)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_umrah_search
    }
}