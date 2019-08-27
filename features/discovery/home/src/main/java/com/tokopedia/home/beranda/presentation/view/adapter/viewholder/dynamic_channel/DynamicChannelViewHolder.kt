package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.TextView

import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DateHelper
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography

abstract class DynamicChannelViewHolder(itemView: View,
                               private val listener: HomeCategoryListener,
                               private val countDownListener: CountDownView.CountDownListener) : AbstractViewHolder<DynamicChannelViewModel>(itemView) {
    private val context: Context = itemView.context

    lateinit var countDownView: CountDownView

    companion object {
        const val TYPE_SPRINT_SALE = 0
        const val TYPE_SPRINT_LEGO = 1
        const val TYPE_ORGANIC = 2
        const val TYPE_SIX_GRID_LEGO = 3
        const val TYPE_THREE_GRID_LEGO = 4
        const val TYPE_CURATED = 5
        const val TYPE_BANNER = 6
        const val TYPE_BANNER_CAROUSEL = 7

        @LayoutRes
        val MASTER_LAYOUT_DC = R.layout.home_master_dynamic_channel
    }

    protected fun getLayoutType(channels: DynamicHomeChannel.Channels): Int {
        when(channels.layout) {
            DynamicHomeChannel.Channels.LAYOUT_6_IMAGE -> return TYPE_SIX_GRID_LEGO
            DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE -> return TYPE_THREE_GRID_LEGO
            DynamicHomeChannel.Channels.LAYOUT_SPRINT -> return TYPE_SPRINT_SALE
            DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO -> return TYPE_SPRINT_LEGO
            DynamicHomeChannel.Channels.LAYOUT_ORGANIC -> return TYPE_ORGANIC
            DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL -> return TYPE_BANNER_CAROUSEL
            DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> return TYPE_BANNER
        }
        return TYPE_CURATED
    }

    override fun bind(element: DynamicChannelViewModel) {
        try {
            val channelTitle: Typography = itemView.findViewById(R.id.channel_title)
            val seeAllButton: TextView = itemView.findViewById(R.id.see_all_button)
            val channelTitleContainer: View = itemView.findViewById(R.id.channel_title_container)
            countDownView = itemView.findViewById(R.id.count_down)

            val channel = element.channel
            val channelHeaderName = element.channel.header.name

            if (!element.isCache) {
                itemView.addOnImpressionListener(channel, OnProductImpressedListener(
                        channel,
                        listener,
                        adapterPosition,
                        getLayoutType(channel)))
            }
            /**
             * add decoration when recyclerview has no item decorator
             */
            setupContent(channel)

            /**
             * Requirement:
             * Only show channel header name when it is exist
             */
            if (!TextUtils.isEmpty(channelHeaderName)) {
                channelTitleContainer.visibility = View.VISIBLE
                channelTitle.text = channelHeaderName
            } else {
                channelTitleContainer.visibility = View.GONE
            }

            /**
             * Requirement:
             * Only show `see all` button when it is exist
             */
            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.header))) {
                seeAllButton.visibility = View.VISIBLE
                seeAllButton.setOnClickListener {
                    listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.header), channel.homeAttribution)
                    HomeTrackingUtils.homeDiscoveryWidgetViewAll(context,
                            DynamicLinkHelper.getActionLink(channel.header))
                    onSeeAllClickTracker(channel, DynamicLinkHelper.getActionLink(channel.getHeader()))
                }
            } else {
                seeAllButton.visibility = View.GONE
            }

            /**
             * Requirement:
             * Only show countDownView when expired time exist
             * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
             *  since onCountDownFinished would getting called and refresh home
             */
            if (hasExpiredTime(channel)) {
                val expiredTime = DateHelper.getExpiredTime(channel.header.expiredTime)
                if (!DateHelper.isExpired(element.serverTimeOffset, expiredTime)) {
                    countDownView.setup(element.serverTimeOffset, expiredTime, countDownListener)
                    countDownView.visibility = View.VISIBLE
                }
            } else {
                countDownView.visibility = View.GONE
            }
        } catch (e: Exception) {
            Crashlytics.log(0, getViewHolderClassName(), e.localizedMessage)
        }
    }

    protected abstract fun setupContent(channel: DynamicHomeChannel.Channels)

    protected abstract fun getViewHolderClassName(): String

    protected abstract fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String)

    private fun hasExpiredTime(channel: DynamicHomeChannel.Channels): Boolean {
        return channel.header.expiredTime != null && !TextUtils.isEmpty(channel.header.expiredTime)
    }

    class OnProductImpressedListener(val channel: DynamicHomeChannel.Channels,
                                     val listener: HomeCategoryListener,
                                     val position: Int,
                                     val layoutType: Int) : ViewHintListener {
        override fun onViewHint() {
            when(layoutType) {
                TYPE_SPRINT_SALE -> {
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionSprintSaleHomePage(
                                    channel.id, channel.grids, channel.homeAttribution, position
                            )
                    )
                }

                TYPE_SPRINT_LEGO, TYPE_ORGANIC -> {
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionDynamicSprintLegoHomePage(
                                    channel.id, channel.grids, channel.header.name, channel.homeAttribution, position
                            )
                    )
                }
                TYPE_SIX_GRID_LEGO -> {
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionLegoBannerHomePage(
                                    channel.id, channel.grids, channel.header.name, channel.homeAttribution, position
                            )
                    )
                }
                TYPE_THREE_GRID_LEGO -> {
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionLegoThreeBannerHomePage(
                                    channel.id, channel.grids, channel.header.name, channel.homeAttribution, position
                            )
                    )
                }
                TYPE_BANNER, TYPE_BANNER_CAROUSEL -> {
                    val bannerType = when(layoutType) {
                        TYPE_BANNER -> "non carousel"
                        TYPE_BANNER_CAROUSEL -> "carousel"
                        else -> "non carousel"
                    }
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionProductChannelMix(
                                    channel.grids, channel.header.name, bannerType
                            )
                    )
                    listener.putEEToIris(
                            channel.enhanceImpressionBannerChannelMix)
                }
            }
        }
    }
}
