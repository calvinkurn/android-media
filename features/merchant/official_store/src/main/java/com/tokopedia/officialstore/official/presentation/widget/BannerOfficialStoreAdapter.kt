package com.tokopedia.officialstore.official.presentation.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.officialstore.R

class BannerOfficialStoreAdapter(bannerImageUrls: List<String> , onPromoClickListener: BannerView.OnPromoClickListener) : BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.widget_official_banner_item, parent, false))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(bannerImageUrls[position])
                .dontAnimate()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.bannerImage)

        holder.bannerImage.setOnClickListener(this.getBannerImageOnClickListener(position))
    }
}