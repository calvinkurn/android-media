package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.EmptyDataModel

class EmptyCardViewHolder(view: View): AbstractViewHolder<EmptyDataModel>(view){

    override fun bind(element: EmptyDataModel?) {
        element?.channel?.banner?.applink?.let{ url ->
            if(url.isNotBlank()) itemView.setOnClickListener {
                HomePageTrackingV2.MixLeft.sendMixLeftBannerClick(element.channel, element.parentPosition)
                RouteManager.route(itemView.context, url)
            }
        }
    }


    companion object{
        val LAYOUT = R.layout.home_banner_item_empty_carousel
    }
}