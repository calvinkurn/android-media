package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.home.R
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPagerAdapter
import com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView.ShimmeringImageView

class HomeBannerAdapter(itemList: List<CircularModel>, listener: CircularListener) : CircularViewPagerAdapter(itemList, listener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        return HomeBannerImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner, parent, false))
    }
}

class HomeBannerImageViewHolder(itemView: View): CircularViewHolder(itemView) {
    override fun bind(item: CircularModel, listener: CircularListener) {
        itemView.findViewById<ShimmeringImageView>(R.id.image).loadImage(item.url)
        itemView.findViewById<ShimmeringImageView>(R.id.image).setOnClickListener { listener.onClick(adapterPosition) }
    }
}