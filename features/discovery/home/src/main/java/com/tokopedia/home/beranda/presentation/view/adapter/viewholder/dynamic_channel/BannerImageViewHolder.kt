package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.ImageView
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.glide.loadGif
import com.tokopedia.home.beranda.listener.HomeCategoryListener

/**
 * Created by Lukas on 17/09/19
 */
class BannerImageViewHolder(itemView: View,
                            homeCategoryListener: HomeCategoryListener,
                            countDownListener: CountDownView.CountDownListener
) : DynamicChannelViewHolder(
        itemView,
        homeCategoryListener,
        countDownListener
) {
    companion object{
        val LAYOUT = R.layout.banner_image
    }

    private val bannerImageView: ImageView by lazy { itemView.findViewById<ImageView>(R.id.banner_image_view)}

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        bannerImageView.loadGif(channel.banner.imageUrl)
//        ImageHandler.loadGifFromUrl(bannerImageView, channel.banner.imageUrl, R.drawable.bannerview_image_placeholder)
        bannerImageView.setOnClickListener {
            RouteManager.route(it.context, channel.banner.applink)
            HomePageTracking.eventEnhanceClickBannerGif(itemView.context, channel)
        }
    }

    override fun getViewHolderClassName(): String {
        return BannerImageViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTracking.eventClickSeeAllGifDCBannerChannel(itemView.context, channel.header.name, channel.id)
    }
}