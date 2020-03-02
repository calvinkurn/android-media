package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.shop.R

/**
 * Created by rizqiaryansa on 2020-02-26.
 */

class ShopHomeSliderBannerAdapter(
        bannerImageUrls: List<String>,
        onPromoClickListener: BannerView.OnPromoClickListener): BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.widget_slider_banner_item, parent, false))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
//        try {
//            Glide.with(holder.itemView.context)
//                    .load(bannerImageUrls[position])
//                    .dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
//                    .centerCrop()
//                    .into(holder.bannerImage)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        ImageHandler.LoadImage(holder.bannerImage, bannerImageUrls[position])

        holder.bannerImage.setOnClickListener(this.getBannerImageOnClickListener(position))
    }
}