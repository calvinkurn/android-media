package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.ImageView
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.glide.loadGif
import com.tokopedia.home.beranda.listener.HomeCategoryListener

/**
 * Created by Lukas on 17/09/19
 */
class BannerImageViewHolder(itemView: View,
                            homeCategoryListener: HomeCategoryListener
) : DynamicChannelViewHolder(
        itemView,
        homeCategoryListener
) {
    companion object{
        val LAYOUT = R.layout.banner_image
    }

    private val bannerImageView: ImageView by lazy { itemView.findViewById<ImageView>(R.id.banner_image_view)}

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        bannerImageView.loadGif(channel.banner.imageUrl)
        bannerImageView.setOnClickListener {
            RouteManager.route(it.context, channel.banner.applink)
            HomePageTracking.eventEnhanceClickBannerGif(channel)
        }
    }

    override fun getViewHolderClassName(): String {
        return BannerImageViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTracking.eventClickSeeAllGifDCBannerChannel(channel.header.name, channel.id)
    }
}