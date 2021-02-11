package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPagerAdapter
import com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView.ShimmeringImageView
import com.tokopedia.home_component.R
import com.tokopedia.unifycomponents.ImageUnify

class BannerChannelAdapter(val itemList: List<CircularModel>, listener: CircularListener) : CircularViewPagerAdapter(itemList, listener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        val layout = if (itemCount > 1) R.layout.layout_banner_channel_item else R.layout.layout_banner_channel_item_full
        return BannerChannelImageViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }
}

class BannerChannelImageViewHolder(itemView: View): CircularViewHolder(itemView) {
    companion object{
        private const val FPM_HOMEPAGE_BANNER = "banner_component_channel"
    }
    override fun bind(item: CircularModel, listener: CircularListener) {
        itemView.findViewById<ImageUnify>(R.id.image_banner_homepage).setImageUrl(url = item.url)
        itemView.findViewById<ImageUnify>(R.id.image_banner_homepage).setOnClickListener { listener.onClick(adapterPosition) }
    }
}