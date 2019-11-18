package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
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

    /**
     * List of possible layout from backend
     */
    companion object {
        const val TYPE_SPRINT_SALE = 0
        const val TYPE_SPRINT_LEGO = 1
        const val TYPE_ORGANIC = 2
        const val TYPE_SIX_GRID_LEGO = 3
        const val TYPE_THREE_GRID_LEGO = 4
        const val TYPE_CURATED = 5
        const val TYPE_BANNER = 6
        const val TYPE_BANNER_CAROUSEL = 7
        const val TYPE_GIF_BANNER = 8
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
            DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> return TYPE_GIF_BANNER

        }
        return TYPE_CURATED
    }

    override fun bind(element: DynamicChannelViewModel) {
        try {
            val channelTitle: Typography = itemView.findViewById(R.id.channel_title)
            val seeAllButton: TextView = itemView.findViewById(R.id.see_all_button)
            val channelTitleContainer: View? = itemView.findViewById(R.id.channel_title_container)
            countDownView = itemView.findViewById(R.id.count_down)

            val channel = element.channel
            val channelHeaderName = element.channel.header.name

            /**
             * Requirement:
             * Only hit impression tracker when get data from cloud
             */
            if (!element.isCache) {
                itemView.addOnImpressionListener(channel, OnItemImpressedListener(
                        channel,
                        listener,
                        adapterPosition,
                        getLayoutType(channel)))
            }

            /**
             * Requirement:
             * Only show channel header name when it is exist
             */
            if (!TextUtils.isEmpty(channelHeaderName)) {
                channelTitleContainer?.visibility = View.VISIBLE
                channelTitle.text = channelHeaderName
                channelTitle.setTextColor(
                        if(channel.header.textColor != null && channel.header.textColor.isNotEmpty()) Color.parseColor(channel.header.textColor)
                        else ContextCompat.getColor(channelTitle.context, R.color.Neutral_N700)
                )
            } else {
                channelTitleContainer?.visibility = View.GONE
            }

            /**
             * Requirement:
             * Only show `see all` button when it is exist
             */
            if (isHasSeeMoreApplink(channel)) {
                seeAllButton.visibility = View.VISIBLE
                seeAllButton.setOnClickListener {
                    listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.header))
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

            /**
             * setup recyclerview content
             */
            setupContent(channel)
        } catch (e: Exception) {
            Crashlytics.log(0, getViewHolderClassName(), e.localizedMessage)
        }
    }

    fun isHasSeeMoreApplink(channel: DynamicHomeChannel.Channels): Boolean {
        return !TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.header))
    }

    /**
     * Abstract function to let child of dynamic channel view holder
     * defines its own list of item
     */
    protected abstract fun setupContent(channel: DynamicHomeChannel.Channels)

    protected abstract fun getViewHolderClassName(): String

    /**
     * Even though tracker only has 1 implementation, but every dynamic channel has different tracker,
     * so you can override this function and apply see all tracker
     */
    protected abstract fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String)

    private fun hasExpiredTime(channel: DynamicHomeChannel.Channels): Boolean {
        return channel.header.expiredTime != null && !TextUtils.isEmpty(channel.header.expiredTime)
    }

    /**
     * Impression listener, list of all dynamic channel tracker impression both for Iris or GTM
     */
    class OnItemImpressedListener(val channel: DynamicHomeChannel.Channels,
                                  val listener: HomeCategoryListener,
                                  val position: Int,
                                  private val layoutType: Int) : ViewHintListener {
        override fun onViewHint() {
            sendIrisTracker(layoutType)
        }

        private fun sendIrisTracker(layoutType: Int) {
            when(layoutType) {
                TYPE_SPRINT_SALE -> {
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionSprintSaleHomePage(
                                    channel.id, channel.grids, position
                            )
                    )
                }

                TYPE_SPRINT_LEGO, TYPE_ORGANIC -> {
                    listener.putEEToIris(
                            HomePageTracking.getIrisEnhanceImpressionDynamicSprintLegoHomePage(
                                    channel.id, channel.grids, channel.header.name
                            )
                    )
                }
                TYPE_SIX_GRID_LEGO -> {
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionLegoBannerHomePage(
                                    channel.id, channel.grids, channel.header.name, position
                            )
                    )
                }
                TYPE_THREE_GRID_LEGO -> {
                    listener.putEEToIris(
                            HomePageTracking.getIrisEnhanceImpressionLegoThreeBannerHomePage(
                                    channel.id, channel.grids, channel.header.name, position
                            )
                    )
                }
                TYPE_GIF_BANNER -> {
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionPromoGifBannerDC(channel)
                    )
                }
                TYPE_BANNER, TYPE_BANNER_CAROUSEL -> {
                    val bannerType = when(layoutType) {
                        TYPE_BANNER -> BannerOrganicViewHolder.TYPE_NON_CAROUSEL
                        TYPE_BANNER_CAROUSEL -> BannerOrganicViewHolder.TYPE_CAROUSEL
                        else -> BannerOrganicViewHolder.TYPE_NON_CAROUSEL
                    }
                    listener.putEEToIris(
                            HomePageTracking.getEnhanceImpressionProductChannelMix(
                                    channel, bannerType
                            )
                    )
                    listener.putEEToIris(
                            HomePageTracking.getIrisEnhanceImpressionBannerChannelMix(channel)
                    )
                }
            }
        }
    }
}
