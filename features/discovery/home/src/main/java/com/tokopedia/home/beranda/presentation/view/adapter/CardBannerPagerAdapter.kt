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
        return BannerViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_slider_banner_design_card, parent, false)
        )
    }
}