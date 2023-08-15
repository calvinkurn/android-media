package com.tokopedia.home_component_header.view

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
import com.tokopedia.home_component_header.R
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.util.DateHelper
import com.tokopedia.home_component_header.util.HomeChannelHeaderRollenceController
import com.tokopedia.home_component_header.util.ViewUtils.convertDpToPixel
import com.tokopedia.home_component_header.util.getLink
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class HomeChannelHeaderView : FrameLayout {
    private var channelHeaderContainer: ConstraintLayout? = null

    private var channelTitle: Typography? = null
    private var channelIconSubtitle: ImageUnify? = null
    private var channelSubtitle: Typography? = null
    private var countDownView: TimerUnifySingle? = null
    private var stubCtaButton: View? = null

    private var headerColorMode: Int = COLOR_MODE_NORMAL
    private var headerCtaMode: Int = CTA_MODE_SEE_ALL
    private var listener: HomeChannelHeaderListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initHeaderWithAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHeaderWithAttrs(attrs)
    }

    private fun initHeaderWithAttrs(attrs: AttributeSet?) {
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeChannelHeaderView)
        try {
            headerColorMode = attributes.getInt(R.styleable.HomeChannelHeaderView_color_mode, COLOR_MODE_NORMAL)
            headerCtaMode = attributes.getInt(R.styleable.HomeChannelHeaderView_cta_mode, CTA_MODE_SEE_ALL)
        } finally {
            attributes.recycle()
        }
    }

    fun bind(
        channelHeader: ChannelHeader,
        listener: HomeChannelHeaderListener? = null,
        colorMode: Int? = null,
        ctaMode: Int? = null
    ) {
        init(channelHeader.layoutStrategy)
        this.listener = listener
        colorMode?.let {
            this.headerColorMode = it
        }
        ctaMode?.let {
            this.headerCtaMode = it
        }
        handleHeaderComponent(channelHeader)
    }

    private fun init(layoutStrategy: HeaderLayoutStrategy) {
        inflate(context, layoutStrategy.getLayout(), this).also {
            channelHeaderContainer = findViewById(R.id.channel_title_container)
        }
    }

    private fun handleHeaderComponent(channelHeader: ChannelHeader) {
        val stubChannelTitle: View? = findViewById(R.id.channel_title)
        val stubCountDownView: View? = findViewById(R.id.count_down)
        val stubSeeAllButton: View? = findViewById(R.id.see_all_button)
        val stubSeeAllButtonUnify: View? = findViewById(R.id.see_all_button_unify)
        val stubChannelSubtitle: View? = findViewById(R.id.channel_subtitle)
        val stubChannelIconSubtitle: View? = findViewById(R.id.channel_subtitle_icon)
        stubCtaButton = findViewById(R.id.cta_button)
        channelHeaderContainer?.let { channelHeaderContainer ->
            handleTitle(channelHeader.name, channelHeaderContainer, stubChannelTitle, channelHeader)
            handleIconSubtitle(stubChannelIconSubtitle, channelHeader)
            handleSubtitle(channelHeader.subtitle, stubChannelSubtitle, channelHeader)
            channelHeader.layoutStrategy.renderCta(
                this,
                channelHeaderContainer,
                stubCtaButton,
                stubSeeAllButton,
                stubSeeAllButtonUnify,
                channelHeader,
                isHasSeeMoreApplink(channelHeader),
                hasExpiredTime(channelHeader),
                listener,
                headerCtaMode,
                headerColorMode
            )
            handleHeaderIconSubtitle(channelHeader, channelHeaderContainer)
            handleHeaderExpiredTime(channelHeader, stubCountDownView, channelHeaderContainer)
            handleBackgroundColor(channelHeader, channelHeaderContainer, stubCtaButton, stubSeeAllButtonUnify)
        }
    }

    private fun handleTitle(channelHeaderName: String?, channelHeaderContainer: ConstraintLayout, stubChannelTitle: View?, channelHeader: ChannelHeader) {
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
            channelHeader.layoutStrategy.renderTitle(
                context,
                channelHeader,
                channelTitle,
                headerColorMode
            )

            val constraintSet = ConstraintSet()
            constraintSet.clone(channelHeaderContainer)
            constraintSet.connect(R.id.channel_title, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
            if (channelHeader.subtitle.isEmpty() && !hasExpiredTime(channelHeader)) {
                constraintSet.connect(R.id.channel_title, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
            } else {
                constraintSet.clear(R.id.channel_title, ConstraintSet.BOTTOM)
            }
            constraintSet.applyTo(channelHeaderContainer)
        } else {
            channelHeaderContainer.visibility = View.GONE
        }
    }

    /**
     * @param icon url
     * @param stubChannelIconSubtitle view
     * @param channelHeader pojo model
     *
     * Only show channel icon subtitle when it is exist
     */
    private fun handleIconSubtitle(stubChannelIconSubtitle: View?, channelHeader: ChannelHeader) {
        if (hasIconSubtitle(channelHeader)) {
            channelIconSubtitle = if (stubChannelIconSubtitle is ViewStub &&
                !isViewStubHasBeenInflated(stubChannelIconSubtitle)
            ) {
                val stubChannelView = stubChannelIconSubtitle.inflate()
                stubChannelView?.findViewById(R.id.channel_subtitle_icon)
            } else {
                null
            }
            channelIconSubtitle?.loadImage(channelHeader.iconSubtitleUrl)
            channelSubtitle?.visibility = View.VISIBLE
        } else {
            channelSubtitle?.visibility = View.GONE
        }
    }

    private fun handleSubtitle(channelSubtitleName: String?, stubChannelSubtitle: View?, channelHeader: ChannelHeader) {
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
            channelHeader.layoutStrategy.renderSubtitle(
                context,
                channelHeader,
                channelSubtitle,
                headerColorMode
            )
        } else {
            channelSubtitle?.visibility = View.GONE
        }
    }

    /**
     * @param channelHeader pojo data
     * @param stubSubtitleView is Typography of subtitle
     * @param channelHeaderContainer is root container for channel header view
     * */
    private fun handleHeaderIconSubtitle(channelHeader: ChannelHeader, channelHeaderContainer: ConstraintLayout) {
        channelHeader.layoutStrategy.setIconSubtitleConstraints(hasIconSubtitle(channelHeader), channelHeaderContainer, context.resources)
    }

    private fun handleHeaderExpiredTime(channelHeader: ChannelHeader, stubCountDownView: View?, channelHeaderContainer: ConstraintLayout) {
        /**
         * Requirement:
         * Only show countDownView when expired time exist
         * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
         *  since onCountDownFinished would getting called and refresh home
         */
        if (hasExpiredTime(channelHeader)) {
            countDownView = if (stubCountDownView is ViewStub &&
                !isViewStubHasBeenInflated(stubCountDownView)
            ) {
                val inflatedStubCountDownView = stubCountDownView.inflate()
                inflatedStubCountDownView.findViewById(R.id.count_down)
            } else {
                findViewById(R.id.count_down)
            }

            val expiredTime = DateHelper.getExpiredTime(channelHeader.expiredTime)
            if (!DateHelper.isExpired(channelHeader.serverTimeOffset, expiredTime)) {
                countDownView?.run {
                    timerVariant = if (channelHeader.backColor.isNotEmpty() || headerColorMode == COLOR_MODE_INVERTED) {
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE
                    // calculate date diff
                    targetDate = Calendar.getInstance().apply {
                        val currentDate = Date()
                        val currentMillisecond: Long = currentDate.time + channelHeader.serverTimeOffset
                        val timeDiff = expiredTime.time - currentMillisecond
                        add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                        add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                        add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                    }
                    onFinish = {
                        listener?.onChannelExpired(channelHeader.channelId)
                    }
                }
            }
        } else {
            countDownView?.let {
                it.visibility = View.GONE
            }
        }
        channelHeader.layoutStrategy.setSubtitleConstraints(hasExpiredTime(channelHeader), channelHeaderContainer, context.resources)
        channelHeader.layoutStrategy.setContainerPadding(channelHeaderContainer, hasExpiredTime(channelHeader), context.resources)
    }

    private fun handleBackgroundColor(channelHeader: ChannelHeader, titleContainer: ConstraintLayout, stubSeeAllButton: View?, stubSeeAllButtonUnify: View?) {
        if (channelHeader.backColor.isNotEmpty()) {
            stubSeeAllButton?.gone()
            stubSeeAllButtonUnify?.gone()
            titleContainer.setBackgroundColor(Color.parseColor(channelHeader.backColor))

            titleContainer.setPadding(
                titleContainer.paddingLeft,
                convertDpToPixel(TITLE_TOP_PADDING, titleContainer.context),
                titleContainer.paddingRight,
                titleContainer.paddingBottom
            )
        }
    }

    private fun isHasSeeMoreApplink(channelHeader: ChannelHeader): Boolean {
        return channelHeader.getLink().isNotEmpty()
    }

    private fun hasExpiredTime(channelHeader: ChannelHeader): Boolean {
        return !TextUtils.isEmpty(channelHeader.expiredTime)
    }

    private fun hasIconSubtitle(channelHeader: ChannelHeader): Boolean {
        return !TextUtils.isEmpty(channelHeader.iconSubtitleUrl)
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }

    fun hideCTAButton() {
        stubCtaButton?.gone()
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
