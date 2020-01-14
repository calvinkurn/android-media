package com.tokopedia.hotel.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.hotel.R
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by furqan on 10/04/19
 */
class HotelLastSearchViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val container: CardUnify = view.findViewById(R.id.item_hotel_last_search_container)
    val imageHotel: AppCompatImageView = view.findViewById(R.id.iv_hotel_homepage_last_search)
    val tvTitle: Typography = view.findViewById(R.id.tg_hotel_homepage_title_last_search)
    val tvSubtitle: Typography = view.findViewById(R.id.tg_hotel_homepage_subtitle_last_search)
    val tvPrefix: Typography = view.findViewById(R.id.tg_hotel_homepage_prefix_last_search)

    fun bind(item: TravelRecentSearchModel.Item) {
        container.layoutParams.height = container.layoutParams.width * 2 / 3
        imageHotel.loadImage(item.imageUrl)
        tvTitle.text = item.title
        tvSubtitle.text = item.subtitle
        tvPrefix.text = item.prefix

        view.setOnClickListener {
            RouteManager.route(view.context, item.appUrl)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_homepage_last_search
    }
}