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
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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

@Deprecated("Please use com.tokopedia.home_component_header.view.HomeChannelHeaderView")
class DynamicChannelHeaderView : FrameLayout {
    private var channelHeaderContainer: ConstraintLayout? = null

    private var channelTitle: Typography? = null
    private var channelSubtitle: Typography? = null
    private var countDownView: TimerUnifySingle? = null

    private var headerColorMode: Int = COLOR_MODE_NORMAL
    private var headerCtaMode: Int = CTA_MODE_SEE_ALL
    private var listener: HeaderListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initHeaderWithAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHeaderWithAttrs(attrs)
    }

    private fun initHeaderWithAttrs(attrs: AttributeSet?) {
            val attributes: TypedArray = context.obtainStyledAttributes(attrs, com.tokopedia.home_component_header.R.styleable.HomeChannelHeaderView)
        try {
            headerColorMode = attributes.getInt(com.tokopedia.home_component_header.R.styleable.HomeChannelHeaderView_color_mode, COLOR_MODE_NORMAL)
            headerCtaMode = attributes.getInt(com.tokopedia.home_component_header.R.styleable.HomeChannelHeaderView_cta_mode, CTA_MODE_SEE_ALL)
        } finally {
            attributes.recycle()
        }
    }

    @Deprecated("Please use com.tokopedia.home_component_header.view.HomeChannelHeaderView.bind()")
    fun setChannel(
        channelModel: ChannelModel,
        listener: HeaderListener,
        colorMode: Int? = null,
        ctaMode: Int? = null
    ) {
        init(channelModel.channelHeader.layoutStrategy)
        this.listener = listener
        colorMode?.let {
            this.headerColorMode = it
        }
        ctaMode?.let {
            this.headerCtaMode = it
        }
        handleHeaderComponent(channelModel)
    }

    private fun init(layoutStrategy: HeaderLayoutStrategy) {
        inflate(context, layoutStrategy.getLayout(), this).also {
            channelHeaderContainer = findViewById(com.tokopedia.home_component_header.R.id.channel_title_container)
        }
    }

    private fun handleHeaderComponent(channel: ChannelModel) {
        val stubChannelTitle: View? = findViewById(com.tokopedia.home_component_header.R.id.channel_title)
        val stubCountDownView: View? = findViewById(com.tokopedia.home_component_header.R.id.count_down)
        val stubSeeAllButton: View? = findViewById(com.tokopedia.home_component_header.R.id.see_all_button)
        val stubSeeAllButtonUnify: View? = findViewById(com.tokopedia.home_component_header.R.id.see_all_button_unify)
        val stubChannelSubtitle: View? = findViewById(com.tokopedia.home_component_header.R.id.channel_subtitle)
        val stubCtaButton: View? = findViewById(com.tokopedia.home_component_header.R.id.cta_button)
        channelHeaderContainer?.let { channelHeaderContainer ->
            handleTitle(channel.channelHeader.name, channelHeaderContainer, stubChannelTitle, channel)
            handleSubtitle(channel.channelHeader.subtitle, stubChannelSubtitle, channel)
            channel.channelHeader.layoutStrategy.renderCta(
                this,
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
            handleBackgroundColor(channel, channelHeaderContainer, stubCtaButton, stubSeeAllButtonUnify)
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
                findViewById(R.id.channel_title)
            }
            channelTitle?.text = channelHeaderName
            channelTitle?.gravity = Gravity.CENTER_VERTICAL
            channelTitle?.visibility = View.VISIBLE
            channel.channelHeader.layoutStrategy.renderTitle(
                context,
                channel.channelHeader,
                channelTitle,
                headerColorMode
            )

            val constraintSet = ConstraintSet()
            constraintSet.clone(channelHeaderContainer)
            constraintSet.connect(R.id.channel_title, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
            if(channel.channelHeader.subtitle.isEmpty() && !hasExpiredTime(channel)) {
                constraintSet.connect(R.id.channel_title, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
            } else {
                constraintSet.clear(R.id.channel_title, ConstraintSet.BOTTOM)
            }
            constraintSet.applyTo(channelHeaderContainer)
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
                findViewById(R.id.channel_subtitle)
            }
            channelSubtitle?.text = channelSubtitleName
            channelSubtitle?.visibility = View.VISIBLE
            channel.channelHeader.layoutStrategy.renderSubtitle(
                context,
                channel.channelHeader,
                channelSubtitle,
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
                findViewById(R.id.count_down)
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
        channel.channelHeader.layoutStrategy.setSubtitleConstraints(hasExpiredTime(channel), channelHeaderContainer, context.resources)
        channel.channelHeader.layoutStrategy.setContainerPadding(channelHeaderContainer, hasExpiredTime(channel), context.resources)
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
