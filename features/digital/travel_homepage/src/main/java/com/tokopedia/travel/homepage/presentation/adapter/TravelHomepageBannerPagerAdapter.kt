package com.tokopedia.travel.homepage.presentation.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.travel.homepage.R
import kotlinx.android.synthetic.main.travel_homepage_slider_banner_design_card.view.*

class TravelHomepageBannerPagerAdapter(bannerImageUrls: List<String>,
                                       onPromoClickListener: BannerView.OnPromoClickListener) :
        BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.travel_homepage_slider_banner_design_card, parent, false)

        val layoutParams = itemView.layoutParams

        if (parent.width != 0) {
            layoutParams.width = (parent.width * 0.9).toInt()
        } else {
            val mWinMgr = itemView.context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = mWinMgr.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x

            layoutParams.width = (width * 0.9).toInt()
        }

        itemView.layoutParams = layoutParams
        itemView.banner_card.visibility = View.VISIBLE

        return BannerViewHolder(itemView)
    }

    override fun getBannerImageId(): Int {
        return R.id.image
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (bannerImageUrls[position] != null && bannerImageUrls[position].isNotEmpty()) {
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