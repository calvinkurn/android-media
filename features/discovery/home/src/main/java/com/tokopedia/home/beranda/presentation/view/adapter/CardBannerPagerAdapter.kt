package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import com.tokopedia.home.R
import kotlinx.android.synthetic.main.layout_slider_banner_design_card.view.*

class CardBannerPagerAdapter(bannerImageUrls : List<String>,
                             onPromoClickListener : BannerView.OnPromoClickListener ) :
        BannerPagerAdapter(bannerImageUrls, onPromoClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.layout_slider_banner_design_card, parent, false)

        val layoutParams = itemView.getLayoutParams()
        layoutParams.width = (parent.width * 0.9).toInt()
        itemView.setLayoutParams(layoutParams)
        itemView.banner_card.visibility = View.VISIBLE

        return BannerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (bannerImageUrls[position] != null && bannerImageUrls[position].length > 0) {
            holder.bannerImage.setOnClickListener(
                    getBannerImageOnClickListener(position)
            )
        }
        try {
            Glide.with(holder.itemView.context)
                    .load(bannerImageUrls[position])
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .centerCrop()
                    .into(holder.bannerImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}