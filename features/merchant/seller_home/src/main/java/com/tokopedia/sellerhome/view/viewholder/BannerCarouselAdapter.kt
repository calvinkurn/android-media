package com.tokopedia.sellerhome.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.sellerhome.R

class BannerCarouselAdapter(
        bannerImageUrls: List<String>,
        onPromoClickListener: BannerView.OnPromoClickListener
) : BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sah_banner_item_layout, parent, false))
    }
}