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
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DateHelper
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.unifyprinciples.Typography
import android.view.ViewStub
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton

abstract class DynamicChannelViewHolder(itemView: View,
                                        private val listener: HomeCategoryListener,
                                        private val countDownListener: CountDownView.CountDownListener) : AbstractViewHolder<DynamicChannelViewModel>(itemView) {
    private val context: Context = itemView.context

    var countDownView: CountDownView? = null
    var seeAllButton: TextView? = null
    var channelTitle: Typography? = null
    var seeAllButtonUnify: UnifyButton? = null
    /**
     * List of possible layout from backend
     */
    companion object {
        const val TYPE_SPRINT_SALE = 0
        const val TYPE_SPRINT_LEGO = 1
        const val TYPE_ORGANIC = 2
        const val TYPE_SIX_GRID_LEGO = 3
        const val TYPE_THREE_GRID_LEGO = 4
        const val TYPE_FOUR_GRID_LEGO = 9
        const val TYPE_CURATED = 5
        const val TYPE_BANNER = 6
        const val TYPE_BANNER_CAROUSEL = 7
        const val TYPE_GIF_BANNER = 8

        fun getLayoutType(channels: DynamicHomeChannel.Channels): Int {
            when(channels.layout) {
                DynamicHomeChannel.Channels.LAYOUT_6_IMAGE -> return TYPE_SIX_GRID_LEGO
                DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE -> return TYPE_THREE_GRID_LEGO
                DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE -> return TYPE_FOUR_GRID_LEGO
                DynamicHomeChannel.Channels.LAYOUT_SPRINT -> return TYPE_SPRINT_SALE
                DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO -> return TYPE_SPRINT_LEGO
                DynamicHomeChannel.Channels.LAYOUT_ORGANIC -> return TYPE_ORGANIC
                DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL -> return TYPE_BANNER_CAROUSEL
                DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> return TYPE_BANNER
                DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> return TYPE_GIF_BANNER

            }
            return TYPE_CURATED
        }
    }

    override fun bind(element: DynamicChannelViewModel) {
        try {
            val channelTitleContainer: View? = itemView.findViewById(R.id.channel_title_container)
            val stubChannelTitle: View? = itemView.findViewById(R.id.channel_title)
            val stubCountDownView: View? = itemView.findViewById(R.id.count_down)
            val stubSeeAllButton: View? = itemView.findViewById(R.id.see_all_button)
            val stubSeeAllButtonUnify: View? = itemView.findViewById(R.id.see_all_button_unify)

            val channel = element.channel
            val channelHeaderName = element.channel?.header?.name

            channel?.let {
                channelTitleContainer?.let {
                    /**
                     * Requirement:
                     * Only show channel header name when it is exist
                     */
                    if (channelHeaderName?.isNotEmpty() == true) {
                        channelTitleContainer.visibility = View.VISIBLE
                        channelTitle = if (stubChannelTitle is ViewStub &&
                                !isViewStubHasBeenInflated(stubChannelTitle)) {
                            val stubChannelView = stubChannelTitle.inflate()
                            stubChannelView?.findViewById(R.id.channel_title)
                        } else {
                            itemView.findViewById(R.id.channel_title)
                        }
                        channelTitle?.text = channelHeaderName
                        channelTitle?.visibility = View.VISIBLE
                        channelTitle?.setTextColor(
                                if(channel.header.textColor.isNotEmpty()) Color.parseColor(channel.header.textColor)
                                else ContextCompat.getColor(context, R.color.Neutral_N700)
                        )
                    } else {
                        channelTitleContainer.visibility = View.GONE
                    }

                    /**
                     * Requirement:
                     * Only show `see all` button when it is exist
                     * Don't show `see all` button on dynamic channel mix carousel
                     */
                    if (isHasSeeMoreApplink(channel) &&
                            getLayoutType(channel) != TYPE_BANNER_CAROUSEL) {
                        seeAllButton = if (stubSeeAllButton is ViewStub &&
                                !isViewStubHasBeenInflated(stubSeeAllButton)) {
                            val stubSeeAllView = stubSeeAllButton.inflate()
                            stubSeeAllView?.findViewById(R.id.see_all_button)
                        } else {
                            itemView.findViewById(R.id.see_all_button)
                        }

                        seeAllButton?.show()
                        seeAllButton?.setOnClickListener {
                            listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.header))
                            HomeTrackingUtils.homeDiscoveryWidgetViewAll(context,
                                    DynamicLinkHelper.getActionLink(channel.header))
                            onSeeAllClickTracker(channel, DynamicLinkHelper.getActionLink(channel.header))
                        }
                    }

                    /**
                     * Requirement:
                     * Show unify button of see more button for dc sprint if back image is not empty
                     */
                    if (channel.header.backImage.isNotBlank() && getLayoutType(channel) == TYPE_SPRINT_LEGO) {
                        seeAllButtonUnify = if (stubSeeAllButtonUnify is ViewStub &&
                                !isViewStubHasBeenInflated(stubSeeAllButtonUnify)) {
                            val stubSeeAllButtonView = stubSeeAllButtonUnify.inflate()
                            stubSeeAllButtonView?.findViewById(R.id.see_all_button_unify)
                        } else {
                            itemView.findViewById(R.id.see_all_button_unify)
                        }

                        seeAllButtonUnify?.show()
                        seeAllButton?.hide()
                    }

                    /**
                     * Requirement:
                     * Only show countDownView when expired time exist
                     * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
                     *  since onCountDownFinished would getting called and refresh home
                     */
                    if (hasExpiredTime(channel)) {
                        countDownView = if (stubCountDownView is ViewStub &&
                                !isViewStubHasBeenInflated(stubCountDownView)) {
                            val inflatedStubCountDownView = stubCountDownView.inflate()
                            inflatedStubCountDownView.findViewById<CountDownView>(R.id.count_down)
                        } else {
                            itemView.findViewById(R.id.count_down)
                        }

                        val expiredTime = DateHelper.getExpiredTime(channel.header.expiredTime)
                        if (!DateHelper.isExpired(element.serverTimeOffset, expiredTime)) {
                            countDownView?.setup(element.serverTimeOffset, expiredTime, countDownListener)
                            countDownView?.visibility = View.VISIBLE
                        }
                    }
                }

                /**
                 * setup recyclerview content
                 */
                setupContent(channel)
            }
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

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }
}
