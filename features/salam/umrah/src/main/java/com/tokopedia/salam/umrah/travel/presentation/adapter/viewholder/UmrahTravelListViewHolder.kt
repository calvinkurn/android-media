package com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import kotlinx.android.synthetic.main.item_umrah_travel_list.view.*

/**
 * @author by Firman on 4/3/20
 */

class UmrahTravelListViewHolder (view: View): AbstractViewHolder<TravelAgent>(view){
    override fun bind(element: TravelAgent) {
        with(itemView){
            iv_widget_umrah_travel_list_item.loadImage(element.imageUrl)
            tg_widget_umrah_travel_list_item_title.text = element.name
            tg_widget_umrah_travel_list_item_desc.text = element.permissionOfUmrah
            tg_widget_umrah_travel_list_item_founded.text = element.ui.establishedSince
        }
    }

    companion object{
        val LAYOUT = R.layout.item_umrah_travel_list
    }
}