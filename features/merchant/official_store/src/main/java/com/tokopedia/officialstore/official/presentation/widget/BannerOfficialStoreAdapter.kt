package com.tokopedia.officialstore.official.presentation.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.official.presentation.listener.BannerListener

class BannerOfficialStoreAdapter(
        bannerImageUrls: List<String>,
        onPromoClickListener: BannerView.OnPromoClickListener) : BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {

    private var bannerListener: BannerListener? = null
    private var officialViewModel: OfficialBannerViewModel? = null

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

        officialViewModel?.let {
            holder.bannerImage.addOnImpressionListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    bannerListener?.putEEToTrackingQueue(
                            officialViewModel?.categoryName?: "",
                            officialViewModel?.banner?.get(position)?.bannerId?: "",
                            position,
                            officialViewModel?.banner?.get(position)?.title?: "",
                            officialViewModel?.banner?.get(position)?.imageUrl?: ""
                    )
                }
            })
        }

        holder.bannerImage.setOnClickListener(this.getBannerImageOnClickListener(position))
    }

    fun setOfficialBannerViewModel(officialViewModel: OfficialBannerViewModel) {
        this.officialViewModel = officialViewModel
    }

}