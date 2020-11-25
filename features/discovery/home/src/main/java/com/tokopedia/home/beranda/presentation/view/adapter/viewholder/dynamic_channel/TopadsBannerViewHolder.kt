package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.load.engine.GlideException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.BannerAdsTracking
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeTopAdsBannerDataModel
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import kotlinx.android.synthetic.main.home_dc_topads_banner.view.*

class TopadsBannerViewHolder(val view: View, val categoryListener: HomeCategoryListener) :
        AbstractViewHolder<HomeTopAdsBannerDataModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_topads_banner
        private const val className = "com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel"
    }

    override fun bind(element: HomeTopAdsBannerDataModel) {
        view.dynamic_channel_header.setChannel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(element.channel, adapterPosition),
                object: HeaderListener {
                    override fun onSeeAllClick(link: String) {

                    }

                    override fun onChannelExpired(channelModel: ChannelModel) {

                    }
                }
        )

        if (element.topAdsImageViewModel == null) {
            view.home_topads_shimmering_loading.visibility = View.VISIBLE
            view.home_topads_image_view.visibility = View.GONE
        } else {
            view.home_topads_shimmering_loading.visibility = View.GONE
            view.home_topads_image_view.visibility = View.VISIBLE
            element.topAdsImageViewModel?.let {
                try {
                    view.home_topads_image_view.loadImage(it) {
                        categoryListener.removeViewHolderAtPosition(adapterPosition)
                    }
                } catch (glideException: GlideException) {
                    categoryListener.removeViewHolderAtPosition(adapterPosition)
                }
            }
        }

        view.home_topads_image_view.setTopAdsImageViewImpression(object: TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                BannerAdsTracking.sendBannerAdsImpressionTracking(
                        categoryListener.getTrackingQueueObj(),
                        element.channel,
                        categoryListener.userId,
                        adapterPosition,
                        false,
                        element.topAdsImageViewModel?.bannerId?:""
                )
                BannerAdsTracking.sendBannerAdsImpressionTracking(
                        categoryListener.getTrackingQueueObj(),
                        element.channel,
                        categoryListener.userId,
                        adapterPosition,
                        true,
                        element.topAdsImageViewModel?.bannerId?:""
                )

                TopAdsUrlHitter(className).hitImpressionUrl(
                        itemView.context,
                        viewUrl,
                        "",
                        "",
                        ""
                )
            }
        })

        view.home_topads_image_view.setTopAdsImageViewClick(object: TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                BannerAdsTracking.sendBannerAdsClickTracking(
                        element.channel,
                        categoryListener.userId,
                        adapterPosition,
                        element.topAdsImageViewModel?.bannerId?:""
                )

                categoryListener.onSectionItemClicked(applink?:"")
            }
        })
    }

    override fun bind(element: HomeTopAdsBannerDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        bind(element)
    }
}