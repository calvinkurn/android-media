package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class SeeMorePdpViewHolder(view: View,
                           private val channels: DynamicHomeChannel.Channels)
    : AbstractViewHolder<SeeMorePdpDataModel>(view){

    private val container: View by lazy { view.findViewById<View>(R.id.container_banner_mix_more) }
    private val bannerBackgroundImage: ImageView by lazy { view.findViewById<ImageView>(R.id.background_banner_mix_more)}

    override fun bind(element: SeeMorePdpDataModel) {
        bannerBackgroundImage.setOnClickListener {
            element.listener.onBannerSeeMoreClicked(applink = element.applink, channel = channels)
        }
        bannerBackgroundImage.loadImageWithoutPlaceholder(element.backgroundImage)
        container.setOnClickListener {
            element.listener.onBannerSeeMoreClicked(applink = element.applink, channel = channels)
        }
    }

    companion object{
        val LAYOUT = R.layout.home_banner_item_carousel_see_more
    }
}