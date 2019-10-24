package com.tokopedia.officialstore.official.presentation.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.officialstore.R

class BannerOfficialStoreAdapter(bannerImageUrls: List<String> , onPromoClickListener: BannerView.OnPromoClickListener) : BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.widget_official_banner_item, parent, false))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        ImageHandler.loadImage(
                holder.itemView.context,
                holder.bannerImage,
                bannerImageUrls[position],
                R.drawable.ic_loading_image
        )

        holder.bannerImage.setOnClickListener(this.getBannerImageOnClickListener(position))
    }
}