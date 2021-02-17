package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DateHelper
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_DYNAMIC_CHANNEL
import com.tokopedia.home.beranda.helper.getTimeDiff
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

abstract class DynamicChannelViewHolder(itemView: View,
                                        private val listener: HomeCategoryListener?) : AbstractViewHolder<DynamicChannelDataModel>(itemView) {
    private val context: Context = itemView.context

    var countDownView: TimerUnifySingle? = null
    var seeAllButton: TextView? = null
    var channelTitle: Typography? = null
    var seeAllButtonUnify: UnifyButton? = null
    var channelSubtitle: TextView? = null

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
        const val TYPE_FOUR_GRID_LEGO = 9
        const val TYPE_MIX_TOP = 10
        const val TYPE_MIX_LEFT = 20
        const val TYPE_RECOMMENDATION_LIST = 14
        const val TYPE_PRODUCT_HIGHLIGHT = 11
        const val TYPE_CATEGORY_WIDGET = 15
        const val TYPE_2_GRID_LEGO = 16

        fun getLayoutType(channels: DynamicHomeChannel.Channels): Int {
            when(channels.layout) {
                DynamicHomeChannel.Channels.LAYOUT_6_IMAGE -> return TYPE_SIX_GRID_LEGO
                DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE -> return TYPE_THREE_GRID_LEGO
                DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE -> return TYPE_FOUR_GRID_LEGO
                DynamicHomeChannel.Channels.LAYOUT_LEGO_2_IMAGE -> return TYPE_2_GRID_LEGO
                DynamicHomeChannel.Channels.LAYOUT_SPRINT -> return TYPE_SPRINT_SALE
                DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO -> return TYPE_SPRINT_LEGO
                DynamicHomeChannel.Channels.LAYOUT_ORGANIC -> return TYPE_ORGANIC
                DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL -> return TYPE_BANNER_CAROUSEL
                DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> return TYPE_BANNER
                DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> return TYPE_GIF_BANNER
                DynamicHomeChannel.Channels.LAYOUT_MIX_TOP -> return TYPE_MIX_TOP
                DynamicHomeChannel.Channels.LAYOUT_PRODUCT_HIGHLIGHT -> return TYPE_PRODUCT_HIGHLIGHT
                DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT -> return TYPE_MIX_LEFT
                DynamicHomeChannel.Channels.LAYOUT_LIST_CAROUSEL -> return TYPE_RECOMMENDATION_LIST
                DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET -> return TYPE_CATEGORY_WIDGET
            }
            return TYPE_CURATED
        }
    }

    override fun bind(element: DynamicChannelDataModel, payloads: MutableList<Any>) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_DYNAMIC_CHANNEL + element.channel?.layout)
        super.bind(element, payloads)
        try {
            val channel = element.channel
            val channelHeaderName = element.channel?.header?.name
            val channelSubtitleName = element.channel?.header?.subtitle

            channel?.let {
                handleHeaderComponent(channelHeaderName, channel, channelSubtitleName, element)
                /**
                 * setup recyclerview content
                 */
                setupContent(channel, payloads)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("E/${getViewHolderClassName()}:${e.localizedMessage}")
        }
        BenchmarkHelper.endSystraceSection()
    }

    override fun bind(element: DynamicChannelDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_DYNAMIC_CHANNEL + element.channel?.layout)
        try {
            val channel = element.channel
            val channelHeaderName = element.channel?.header?.name
            val channelSubtitleName = element.channel?.header?.subtitle

            channel?.let {
                handleHeaderComponent(channelHeaderName, channel, channelSubtitleName, element)
                /**
                 * setup recyclerview content
                 */
                setupContent(channel)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("E/${getViewHolderClassName()}:${e.localizedMessage}")
        }
        BenchmarkHelper.endSystraceSection()
    }

    private fun handleHeaderComponent(channelHeaderName: String?, channel: DynamicHomeChannel.Channels, channelSubtitleName: String?, element: DynamicChannelDataModel) {
        val channelTitleContainer: ConstraintLayout? = itemView.findViewById(R.id.channel_title_container)
        val stubChannelTitle: View? = itemView.findViewById(R.id.channel_title)
        val stubCountDownView: View? = itemView.findViewById(R.id.count_down)
        val stubSeeAllButton: View? = itemView.findViewById(R.id.see_all_button)
        val stubSeeAllButtonUnify: View? = itemView.findViewById(R.id.see_all_button_unify)
        val stubChannelSubtitle: View? = itemView.findViewById(R.id.channel_subtitle)
        channelTitleContainer?.let {
            handleTitle(channelHeaderName, channelTitleContainer, stubChannelTitle, channel)
            handleSubtitle(channelSubtitleName, stubChannelSubtitle, channel)
            handleSeeAllApplink(channel, stubSeeAllButton, channelSubtitleName, channelTitleContainer)
            handleBackImage(channel, stubSeeAllButtonUnify, channelSubtitleName, channelTitleContainer)
            handleHeaderExpiredTime(channel, stubCountDownView, element)
        }
    }

    private fun handleTitle(channelHeaderName: String?, channelTitleContainer: ConstraintLayout, stubChannelTitle: View?, channel: DynamicHomeChannel.Channels) {
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
                    if (channel.header.textColor.isNotEmpty()) Color.parseColor(channel.header.textColor).invertIfDarkMode(itemView.context)
                    else ContextCompat.getColor(context, R.color.Unify_N700).invertIfDarkMode(itemView.context)
            )
        } else {
            channelTitleContainer.visibility = View.GONE
        }
    }

    private fun handleSubtitle(channelSubtitleName: String?, stubChannelSubtitle: View?, channel: DynamicHomeChannel.Channels) {
        /**
         * Requirement:
         * Only show channel subtitle when it is exist
         */
        if (channelSubtitleName?.isNotEmpty() == true) {
            channelSubtitle = if (stubChannelSubtitle is ViewStub &&
                    !isViewStubHasBeenInflated(stubChannelSubtitle)) {
                val stubChannelView = stubChannelSubtitle.inflate()
                stubChannelView?.findViewById(R.id.channel_subtitle)
            } else {
                itemView.findViewById(R.id.channel_subtitle)
            }
            channelSubtitle?.text = channelSubtitleName
            channelSubtitle?.visibility = View.VISIBLE
            channelSubtitle?.setTextColor(
                    if (channel.header.textColor.isNotEmpty()) Color.parseColor(channel.header.textColor).invertIfDarkMode(itemView.context)
                    else ContextCompat.getColor(context, R.color.Unify_N700).invertIfDarkMode(itemView.context)
            )
        } else {
            channelSubtitle?.visibility = View.GONE
        }
    }

    private fun handleSeeAllApplink(channel: DynamicHomeChannel.Channels, stubSeeAllButton: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
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

            handleSubtitlePosition(channelSubtitleName, channel, channelTitleContainer)

            seeAllButton?.show()
            seeAllButton?.setOnClickListener {
                listener?.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.header))
                HomeTrackingUtils.homeDiscoveryWidgetViewAll(context,
                        DynamicLinkHelper.getActionLink(channel.header))
                onSeeAllClickTracker(channel, DynamicLinkHelper.getActionLink(channel.header))
            }
        }
    }

    private fun handleSubtitlePosition(channelSubtitleName: String?, channel: DynamicHomeChannel.Channels, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * `see all` button align to subtitle and countdown timer
         */
        if (channelSubtitleName?.isEmpty() != false && !hasExpiredTime(channel)) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelTitleContainer)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, R.id.channel_title, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelTitleContainer)
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelTitleContainer)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.count_down, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, R.id.count_down, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelTitleContainer)
        }
    }

    private fun handleBackImage(channel: DynamicHomeChannel.Channels, stubSeeAllButtonUnify: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
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

            handleUnifySeeAllButton(channelSubtitleName, channel, channelTitleContainer)
        }
    }

    private fun handleUnifySeeAllButton(channelSubtitleName: String?, channel: DynamicHomeChannel.Channels, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * `see all unify` button align to subtitle and countdown timer
         */
        if (channelSubtitleName?.isEmpty() != false && !hasExpiredTime(channel)) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelTitleContainer)
            constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.BOTTOM, R.id.channel_title, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelTitleContainer)
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelTitleContainer)
            constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.TOP, R.id.count_down, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.see_all_button_unify, ConstraintSet.BOTTOM, R.id.count_down, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelTitleContainer)
        }

        seeAllButtonUnify?.show()
        seeAllButton?.hide()
    }

    private fun handleHeaderExpiredTime(channel: DynamicHomeChannel.Channels, stubCountDownView: View?, element: DynamicChannelDataModel) {
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
                inflatedStubCountDownView.findViewById(R.id.count_down)
            } else {
                itemView.findViewById(R.id.count_down)
            }

            val expiredTime = DateHelper.getExpiredTime(channel.header.expiredTime)
            if (!DateHelper.isExpired(element.serverTimeOffset, expiredTime)) {
                val currentDate = Date()
                val currentMillisecond: Long = currentDate.time + element.serverTimeOffset
                val serverTime = Date()
                serverTime.time = currentMillisecond
                val timeDiff = serverTime.getTimeDiff(expiredTime)
                countDownView?.targetDate = timeDiff
                countDownView?.onFinish = {
                    listener?.updateExpiredChannel(element, adapterPosition)
                }
                if(channel.header.backColor.isNotEmpty()){
                    countDownView?.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
                } else {
                    countDownView?.timerVariant = TimerUnifySingle.VARIANT_MAIN
                }
                countDownView?.visibility = View.VISIBLE
            }
        } else {
            countDownView?.let {
                it.visibility = View.GONE
            }
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

    protected open fun setupContent(channel: DynamicHomeChannel.Channels, payloads: MutableList<Any>) {
        setupContent(channel)
    }

    protected abstract fun getViewHolderClassName(): String

    /**
     * Even though tracker only has 1 implementation, but every dynamic channel has different tracker,
     * so you can override this function and apply see all tracker
     */
    protected abstract fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String)

    private fun hasExpiredTime(channel: DynamicHomeChannel.Channels): Boolean {
        return !TextUtils.isEmpty(channel.header.expiredTime)
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }
}
