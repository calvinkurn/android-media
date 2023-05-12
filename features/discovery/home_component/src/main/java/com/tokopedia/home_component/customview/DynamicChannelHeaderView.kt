package com.tokopedia.home_component.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.home_component.HomeComponentRollenceController
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.header.HeaderLayoutStrategy
import com.tokopedia.home_component.customview.header.HeaderLayoutStrategyFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.home_component.util.getLink
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class DynamicChannelHeaderView : FrameLayout {
    private val itemView: View

    private val channelHeaderContainer: ConstraintLayout
    private var channelTitle: Typography? = null
    private var channelSubtitle: TextView? = null
    private var countDownView: TimerUnifySingle? = null

    private var headerColorMode: Int = COLOR_MODE_NORMAL
    private var headerCtaMode: Int = CTA_MODE_SEE_ALL
    private var listener: HeaderListener? = null

    private val isUsingHeaderRevamp = HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant()
    private val layoutStrategy: HeaderLayoutStrategy = HeaderLayoutStrategyFactory.create(isUsingHeaderRevamp)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initHeaderWithAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHeaderWithAttrs(attrs)
    }

    init {
        val view = LayoutInflater.from(context).inflate(layoutStrategy.getLayout(), this)
        this.itemView = view

        channelHeaderContainer = view.findViewById(R.id.channel_title_container)
    }

    private fun initHeaderWithAttrs(attrs: AttributeSet?) {
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicChannelHeaderRevampView)
        try {
            headerColorMode = attributes.getInt(R.styleable.DynamicChannelHeaderRevampView_color_mode, COLOR_MODE_NORMAL)
            headerCtaMode = attributes.getInt(R.styleable.DynamicChannelHeaderRevampView_cta_mode, CTA_MODE_SEE_ALL)
        } finally {
            attributes.recycle()
        }
    }

    fun setChannel(
        channelModel: ChannelModel,
        listener: HeaderListener,
        colorMode: Int? = null,
        ctaMode: Int? = null
    ) {
        this.listener = listener
        colorMode?.let {
            this.headerColorMode = it
        }
        ctaMode?.let {
            this.headerCtaMode = it
        }
        handleHeaderComponent(channelModel)
    }

    private fun handleHeaderComponent(channel: ChannelModel) {
        val stubChannelTitle: View? = itemView.findViewById(R.id.channel_title)
        val stubCountDownView: View? = itemView.findViewById(R.id.count_down)
        val stubSeeAllButton: View? = itemView.findViewById(R.id.see_all_button)
        val stubSeeAllButtonUnify: View? = itemView.findViewById(R.id.see_all_button_unify)
        val stubChannelSubtitle: View? = itemView.findViewById(R.id.channel_subtitle)
        val stubCtaButton: View? = itemView.findViewById(R.id.cta_button)
        channelHeaderContainer.let {
            handleTitle(channel.channelHeader.name, channelHeaderContainer, stubChannelTitle, channel)
            handleSubtitle(channel.channelHeader.subtitle, stubChannelSubtitle, channel)
            layoutStrategy.renderCta(
                itemView,
                channelHeaderContainer,
                stubCtaButton,
                stubSeeAllButton,
                stubSeeAllButtonUnify,
                channel,
                isHasSeeMoreApplink(channel),
                hasExpiredTime(channel),
                listener,
                headerCtaMode,
                headerColorMode
            )
            handleHeaderExpiredTime(channel, stubCountDownView, channelHeaderContainer)
            handleBackgroundColor(channel, it, stubCtaButton, stubSeeAllButtonUnify)
        }
    }

    private fun handleTitle(channelHeaderName: String?, channelHeaderContainer: ConstraintLayout, stubChannelTitle: View?, channel: ChannelModel) {
        /**
         * Requirement:
         * Only show channel header name when it is exist
         */
        if (channelHeaderName?.isNotEmpty() == true) {
            channelHeaderContainer.visibility = View.VISIBLE
            channelTitle = if (stubChannelTitle is ViewStub &&
                !isViewStubHasBeenInflated(stubChannelTitle)
            ) {
                val stubChannelView = stubChannelTitle.inflate()
                stubChannelView?.findViewById(R.id.channel_title)
            } else {
                itemView.findViewById(R.id.channel_title)
            }
            channelTitle?.text = channelHeaderName
            channelTitle?.gravity = Gravity.CENTER_VERTICAL
            channelTitle?.visibility = View.VISIBLE
            layoutStrategy.renderTitle(
                context,
                channel.channelHeader,
                channelTitle,
                headerColorMode
            )
        } else {
            channelHeaderContainer.visibility = View.GONE
        }
    }

    private fun handleSubtitle(channelSubtitleName: String?, stubChannelSubtitle: View?, channel: ChannelModel) {
        /**
         * Requirement:
         * Only show channel subtitle when it is exist
         */
        if (channelSubtitleName?.isNotEmpty() == true) {
            channelSubtitle = if (stubChannelSubtitle is ViewStub &&
                !isViewStubHasBeenInflated(stubChannelSubtitle)
            ) {
                val stubChannelView = stubChannelSubtitle.inflate()
                stubChannelView?.findViewById(R.id.channel_subtitle)
            } else {
                itemView.findViewById(R.id.channel_subtitle)
            }
            channelSubtitle?.text = channelSubtitleName
            channelSubtitle?.visibility = View.VISIBLE
            layoutStrategy.renderSubtitle(
                context,
                channel.channelHeader,
                channelTitle,
                headerColorMode
            )
        } else {
            channelSubtitle?.visibility = View.GONE
        }
    }

    private fun handleHeaderExpiredTime(channel: ChannelModel, stubCountDownView: View?, channelHeaderContainer: ConstraintLayout) {
        /**
         * Requirement:
         * Only show countDownView when expired time exist
         * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
         *  since onCountDownFinished would getting called and refresh home
         */
        if (hasExpiredTime(channel)) {
            countDownView = if (stubCountDownView is ViewStub &&
                !isViewStubHasBeenInflated(stubCountDownView)
            ) {
                val inflatedStubCountDownView = stubCountDownView.inflate()
                inflatedStubCountDownView.findViewById(R.id.count_down)
            } else {
                itemView.findViewById(R.id.count_down)
            }

            val expiredTime = DateHelper.getExpiredTime(channel.channelHeader.expiredTime)
            if (!DateHelper.isExpired(channel.channelConfig.serverTimeOffset, expiredTime)) {
                countDownView?.run {
                    timerVariant = if (channel.channelHeader.backColor.isNotEmpty() || headerColorMode == COLOR_MODE_INVERTED) {
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE
                    // calculate date diff
                    targetDate = Calendar.getInstance().apply {
                        val currentDate = Date()
                        val currentMillisecond: Long = currentDate.time + channel.channelConfig.serverTimeOffset
                        val timeDiff = expiredTime.time - currentMillisecond
                        add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                        add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                        add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                    }
                    onFinish = {
                        listener?.onChannelExpired(channel)
                    }
                }
            }
        } else {
            countDownView?.let {
                it.visibility = View.GONE
            }
        }
        layoutStrategy.setSubtitleConstraints(hasExpiredTime(channel), channelHeaderContainer, context.resources)
        layoutStrategy.setContainerPadding(channelHeaderContainer, hasExpiredTime(channel), context.resources)
    }

    private fun handleBackgroundColor(channel: ChannelModel, titleContainer: ConstraintLayout, stubSeeAllButton: View?, stubSeeAllButtonUnify: View?) {
        if (channel.channelHeader.backColor.isNotEmpty()) {
            stubSeeAllButton?.gone()
            stubSeeAllButtonUnify?.gone()
            titleContainer.setBackgroundColor(Color.parseColor(channel.channelHeader.backColor))

            titleContainer.setPadding(
                titleContainer.paddingLeft,
                convertDpToPixel(TITLE_TOP_PADDING, titleContainer.context),
                titleContainer.paddingRight,
                titleContainer.paddingBottom
            )
        }
    }

    private fun isHasSeeMoreApplink(channel: ChannelModel): Boolean {
        return channel.channelHeader.getLink().isNotEmpty()
    }

    private fun hasExpiredTime(channel: ChannelModel): Boolean {
        return !TextUtils.isEmpty(channel.channelHeader.expiredTime)
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }

    companion object {
        private const val TITLE_TOP_PADDING = 15f

        const val COLOR_MODE_NORMAL = 0
        const val COLOR_MODE_INVERTED = 1

        const val CTA_MODE_SEE_ALL = 0
        const val CTA_MODE_RELOAD = 1
        const val CTA_MODE_CLOSE = 2
    }
}
