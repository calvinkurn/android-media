package com.tokopedia.officialstore.official.presentation.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.officialstore.R

class BannerOfficialStoreAdapter(
        bannerImageUrls: List<String>,
        onPromoClickListener: BannerView.OnPromoClickListener) : BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {

//    private var bannerListener: BannerListener? = null
//    private var officialBannerViewModel: OfficialBannerViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.widget_official_banner_item, parent, false))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        try {
            Glide.with(holder.itemView.context)
                    .load(bannerImageUrls[position])
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                    .centerCrop()
                    .into(holder.bannerImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        officialBannerViewModel?.let {
//            holder.bannerImage.addOnImpressionListener(it, object : ViewHintListener {
//                override fun onViewHint() {
//                    bannerListener?.putEEToTrackingQueue(
//                            officialBannerViewModel?.categoryName?: "",
//                            officialBannerViewModel?.banner?.get(position)?.bannerId?: "",
//                            position,
//                            officialBannerViewModel?.banner?.get(position)?.title?: "",
//                            officialBannerViewModel?.banner?.get(position)?.imageUrl?: ""
//                    )
//                }
//            })
//        }

        holder.bannerImage.setOnClickListener(this.getBannerImageOnClickListener(position))
    }

//    fun setOfficialBannerViewModel(officialViewModel: OfficialBannerViewModel) {
//        this.officialBannerViewModel = officialViewModel
//    }

//    fun getOfficialBannerViewModel(): OfficialBannerViewModel? {
//        return officialBannerViewModel
//    }

}