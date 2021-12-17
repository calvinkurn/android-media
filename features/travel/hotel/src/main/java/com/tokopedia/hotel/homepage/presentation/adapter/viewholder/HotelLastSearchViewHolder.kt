package com.tokopedia.hotel.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.hotel.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by furqan on 10/04/19
 */
class HotelLastSearchViewHolder(val view: View, val listener: LastSearchListener) : RecyclerView.ViewHolder(view) {
    private val container: CardUnify = view.findViewById(R.id.item_hotel_last_search_container)
    private val imageHotel: AppCompatImageView = view.findViewById(R.id.iv_hotel_homepage_last_search)
    private val tvTitle: Typography = view.findViewById(R.id.tg_hotel_homepage_title_last_search)
    private val tvSubtitle: Typography = view.findViewById(R.id.tg_hotel_homepage_subtitle_last_search)
    private val tvPrefix: Typography = view.findViewById(R.id.tg_hotel_homepage_prefix_last_search)

    fun bind(item: TravelRecentSearchModel.Item) {
        listener.onItemBind(item, adapterPosition)
        container.layoutParams.height = container.layoutParams.width * 2 / 3
        imageHotel.loadImage(item.imageUrl)
        tvTitle.text = item.title
        tvSubtitle.text = item.subtitle
        tvPrefix.text = item.prefix

        view.setOnClickListener {
            listener.onItemClick(item, adapterPosition)
            RouteManager.route(view.context, item.appUrl)
        }
    }

    interface LastSearchListener {
        fun onItemBind(item: TravelRecentSearchModel.Item, position: Int)
        fun onItemClick(item: TravelRecentSearchModel.Item, position: Int)
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_homepage_last_search
    }
}