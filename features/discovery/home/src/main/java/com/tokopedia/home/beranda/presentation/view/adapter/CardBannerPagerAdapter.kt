package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import com.tokopedia.home.R

class CardBannerPagerAdapter(bannerImageUrls : List<String>,
                             onPromoClickListener : BannerView.OnPromoClickListener ) :
        BannerPagerAdapter(bannerImageUrls, onPromoClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.layout_slider_banner_design_card, parent, false)

        val layoutParams = itemView.getLayoutParams()
        layoutParams.width = (parent.width * 0.9).toInt()
        itemView.setLayoutParams(layoutParams)

        return BannerViewHolder(itemView)
    }
}