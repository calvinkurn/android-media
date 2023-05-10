package com.tokopedia.home_component.customview

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.HomeComponentRollenceController
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.home_component.util.getLink
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class DynamicChannelHeaderView: FrameLayout {
    private val itemView: View
    
    private val channelHeaderContainer: ConstraintLayout
    private var channelTitle: Typography? = null
    private var channelSubtitle: TextView? = null
    private var countDownView: TimerUnifySingle? = null
    private var seeAllButton: TextView? = null
    private var seeAllButtonUnify: UnifyButton? = null
    // New version of CTA button
    private var ctaButtonRevamp: IconUnify? = null
    private var ctaButtonRevampContainer: LinearLayout? = null
    // Rotate animation for
    private val rotateAnimation by lazy {
        RotateAnimation(ROTATE_FROM_DEGREES, ROTATE_TO_DEGREES, Animation.RELATIVE_TO_SELF, PIVOT_X_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_Y_VALUE)
            .apply {
                duration = ROTATE_DURATION
                interpolator = LinearInterpolator()
            }
    }
    private var headerColorMode: Int? = null
    private var headerCtaMode: Int? = null
    private var listener: HeaderListener? = null
    private val isUsingHeaderRevamp by lazy { HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant() }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initHeaderWithAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHeaderWithAttrs(attrs)
    }

    init {
        val layout = if(isUsingHeaderRevamp)
            R.layout.home_component_dynamic_channel_header_revamp
        else R.layout.home_component_dynamic_channel_header
        val view = LayoutInflater.from(context).inflate(layout, this)
        this.itemView = view
        
        channelHeaderContainer = view.findViewById(R.id.channel_title_container)
    }

    private fun initHeaderWithAttrs(attrs: AttributeSet?) {
        val attributes : TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicChannelHeaderRevampView)
        headerColorMode = attributes.getInt(R.styleable.DynamicChannelHeaderRevampView_color_mode, COLOR_MODE_NORMAL)
        headerCtaMode = attributes.getInt(R.styleable.DynamicChannelHeaderRevampView_cta_mode, CTA_MODE_SEE_ALL)
    }

    fun setChannel(
        channelModel: ChannelModel,
        listener: HeaderListener,
        colorMode: Int? = null,
        ctaMode: Int? = null
    ) {
        this.listener = listener
        if(this.headerColorMode == null) {
            this.headerColorMode = colorMode ?: COLOR_MODE_NORMAL
        }
        if(this.headerCtaMode == null) {
            this.headerCtaMode = ctaMode ?: CTA_MODE_SEE_ALL
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
            if(isUsingHeaderRevamp) {
                handleCtaRevamp(channel, stubCtaButton, channel.channelHeader.subtitle, channelHeaderContainer)
            } else {
                handleSeeAllApplink(channel, stubSeeAllButton, channel.channelHeader.subtitle, channelHeaderContainer)
            }
            handleBackImage(channel, stubSeeAllButtonUnify, channel.channelHeader.subtitle, channelHeaderContainer)
            handleHeaderExpiredTime(channel, stubCountDownView, channelHeaderContainer)
            handleBackgroundColor(channel, it, stubSeeAllButton, stubSeeAllButtonUnify)
        }
    }

    private fun handleTitle(channelHeaderName: String?, channelTitleContainer: ConstraintLayout, stubChannelTitle: View?, channel: ChannelModel) {
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
            channelTitle?.gravity = Gravity.CENTER_VERTICAL
            channelTitle?.visibility = View.VISIBLE
            if(isUsingHeaderRevamp && channel.style == ChannelStyle.ChannelHome) {
                channelTitle?.setTextColor(
                    if (headerColorMode == COLOR_MODE_INVERTED) ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
                    else if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).staticIfDarkMode(itemView.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
                adjustTitleVerticalPosition(channel)
            } else {
                channelTitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).invertIfDarkMode(itemView.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700).invertIfDarkMode(itemView.context)
                )
            }
        } else {
            channelTitleContainer.visibility = View.GONE
        }
    }

    private fun handleSubtitle(channelSubtitleName: String?, stubChannelSubtitle: View?, channel: ChannelModel) {
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
            if(isUsingHeaderRevamp) {
                channelSubtitle?.setTextColor(
                    if (headerColorMode == COLOR_MODE_INVERTED) ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
                    else if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).staticIfDarkMode(itemView.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                )
            } else {
                channelSubtitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).invertIfDarkMode(itemView.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700).invertIfDarkMode(itemView.context)
                )
            }
        } else {
            channelSubtitle?.visibility = View.GONE
        }
    }

    private fun handleSeeAllApplink(channel: ChannelModel, stubSeeAllButton: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * Only show `see all` button when it is exist
         * Don't show `see all` button on dynamic channel mix carousel
         */
        ctaButtonRevampContainer?.hide()
        if (isHasSeeMoreApplink(channel)) {
            seeAllButton = if (stubSeeAllButton is ViewStub &&
                !isViewStubHasBeenInflated(stubSeeAllButton)) {
                val stubSeeAllView = stubSeeAllButton.inflate()
                stubSeeAllView?.findViewById(R.id.see_all_button)
            } else {
                itemView.findViewById(R.id.see_all_button)
            }

            handleSeeAllPosition(channelSubtitleName, channel, channelTitleContainer)

            if(channel.style == ChannelStyle.ChannelHome){
                seeAllButton?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            } else if(channel.style == ChannelStyle.ChannelOS){
                seeAllButton?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_P600))
            }

            seeAllButton?.show()
            seeAllButton?.setOnClickListener {
                listener?.onSeeAllClick(channel.channelHeader.getLink())
            }
        } else {
            seeAllButton?.hide()
        }
    }

    private fun handleCtaRevamp(channel: ChannelModel, stubCtaButton: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
        seeAllButton?.hide()
        if (isHasSeeMoreApplink(channel) || headerCtaMode != CTA_MODE_SEE_ALL) {
            if (stubCtaButton is ViewStub &&
                !isViewStubHasBeenInflated(stubCtaButton)) {
                stubCtaButton.inflate()?.initializeCtaRevamp()
            } else {
                itemView.initializeCtaRevamp()
            }
            setHeaderRevampCtaIcon()
            ctaButtonRevampContainer?.show()
            ctaButtonRevamp?.setOnClickListener {
                when(headerCtaMode) {
                    CTA_MODE_SEE_ALL -> listener?.onSeeAllClick(channel.channelHeader.getLink())
                    CTA_MODE_RELOAD -> {
                        it.startAnimation(rotateAnimation)
                        listener?.onReloadClick(channel)
                    }
                    CTA_MODE_CLOSE -> listener?.onDismissClick(channel)
                }
            }
            handleSeeAllPosition(channelSubtitleName, channel, channelTitleContainer)
        } else {
            ctaButtonRevampContainer?.hide()
        }
    }

    private fun setHeaderRevampCtaIcon() {
        val iconId = when(headerCtaMode) {
            CTA_MODE_SEE_ALL -> IconUnify.CHEVRON_RIGHT
            CTA_MODE_RELOAD -> IconUnify.RELOAD
            CTA_MODE_CLOSE -> IconUnify.CLOSE
            else -> IconUnify.CHEVRON_RIGHT
        }
        when(headerColorMode) {
            COLOR_MODE_NORMAL -> {
                (ctaButtonRevampContainer?.background as? GradientDrawable)?.setColor(com.tokopedia.unifyprinciples.R.color.Unify_NN200)
                ctaButtonRevamp?.setImage(
                    newIconId = iconId,
                    newLightEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900),
                    newDarkEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
            }
            COLOR_MODE_INVERTED -> {
                (ctaButtonRevampContainer?.background as? GradientDrawable)?.setColor(com.tokopedia.home_component.R.color.home_dms_header_see_all_border_inverted)
                ctaButtonRevamp?.setImage(
                    newIconId = iconId,
                    newLightEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White),
                    newDarkEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
                )
            }
        }
    }

    private fun handleSeeAllPosition(channelSubtitleName: String?, channel: ChannelModel, channelTitleContainer: ConstraintLayout?) {
        if(isUsingHeaderRevamp) {
            /**
             * Requirement (revamp):
             * `see all` button align to center between title and subtitle/countdown timer
             */
            val bottomAnchor = if (channelSubtitleName?.isEmpty() == true && !hasExpiredTime(channel))
                R.id.channel_title
            else if(hasExpiredTime(channel))
                R.id.count_down
            else R.id.channel_subtitle
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelTitleContainer)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, bottomAnchor, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelTitleContainer)
        } else {
            /**
             * Requirement (control):
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
    }

    private fun handleBackImage(channel: ChannelModel, stubSeeAllButtonUnify: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * Show unify button of see more button for dc sprint if back image is not empty
         */
        if (channel.channelHeader.backImage.isNotBlank() && !isUsingHeaderRevamp) {
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

    private fun handleUnifySeeAllButton(channelSubtitleName: String?, channel: ChannelModel, channelTitleContainer: ConstraintLayout?) {
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

    private fun handleHeaderExpiredTime(channel: ChannelModel, stubCountDownView: View?, channelTitleContainer: ConstraintLayout) {
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

            val expiredTime = DateHelper.getExpiredTime(channel.channelHeader.expiredTime)
            if (!DateHelper.isExpired(channel.channelConfig.serverTimeOffset, expiredTime)) {
                countDownView?.run {
                    timerVariant = if(channel.channelHeader.backColor.isNotEmpty() || headerColorMode == COLOR_MODE_INVERTED){
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE
                    if(isUsingHeaderRevamp) {
                        anchorSubtitleToTimer()
                        channelTitleContainer.setPadding(channelTitleContainer.paddingLeft, channelTitleContainer.paddingTop, channelTitleContainer.paddingRight, context.resources.getDimensionPixelSize(R.dimen.home_dynamic_header_bottom_padding_with_timer))
                    } else {
                        channelTitleContainer.setPadding(channelTitleContainer.paddingLeft, channelTitleContainer.paddingTop, channelTitleContainer.paddingRight, 8f.toDpInt())
                    }
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
                channelTitleContainer.setPadding(channelTitleContainer.paddingLeft, channelTitleContainer.paddingTop, channelTitleContainer.paddingRight, 12f.toDpInt())
            }
            if(isUsingHeaderRevamp) {
                anchorSubtitleToTitle()
                channelTitleContainer.setPadding(channelTitleContainer.paddingLeft, channelTitleContainer.paddingTop, channelTitleContainer.paddingRight, context.resources.getDimensionPixelSize(R.dimen.home_dynamic_header_bottom_padding_without_timer))
            }
        }
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
                    titleContainer.paddingBottom)
        }
    }

    fun hideTitleAndSubtitle() {
        channelTitle?.invisible()
        channelSubtitle?.invisible()
    }

    private fun isHasSeeMoreApplink(channel: ChannelModel): Boolean {
        return channel.channelHeader.getLink().isNotEmpty()
    }

    private fun View.initializeCtaRevamp() {
        ctaButtonRevamp = findViewById(R.id.cta_button_revamp)
        ctaButtonRevampContainer = findViewById(R.id.cta_border_revamp)
    }

    private fun hasExpiredTime(channel: ChannelModel): Boolean {
        return !TextUtils.isEmpty(channel.channelHeader.expiredTime)
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }

    private fun Int.staticIfDarkMode(context: Context?): Int {
        return if(context != null && context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950) else this
    }

    private fun adjustTitleVerticalPosition(channel: ChannelModel) {
        if(hasExpiredTime(channel) || channel.channelHeader.subtitle.isNotEmpty()) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelHeaderContainer)
            constraintSet.clear(R.id.channel_title, ConstraintSet.BOTTOM)
            constraintSet.applyTo(channelHeaderContainer)
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelHeaderContainer)
            constraintSet.connect(R.id.channel_title, ConstraintSet.BOTTOM, channelHeaderContainer.id, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelHeaderContainer)
        }
    }
    
    private fun anchorSubtitleToTitle() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(channelHeaderContainer)
        constraintSet.connect(R.id.channel_subtitle, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.BOTTOM, context.resources.getDimensionPixelSize(R.dimen.home_dynamic_header_subtitle_top_padding))
        constraintSet.connect(R.id.channel_subtitle, ConstraintSet.BOTTOM, channelHeaderContainer.id, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(channelHeaderContainer)
    }

    private fun anchorSubtitleToTimer() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(channelHeaderContainer)
        constraintSet.connect(R.id.channel_subtitle, ConstraintSet.TOP, R.id.count_down, ConstraintSet.TOP, 0)
        constraintSet.connect(R.id.channel_subtitle, ConstraintSet.BOTTOM, R.id.count_down, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(channelHeaderContainer)
    }

    companion object {
        private const val TITLE_TOP_PADDING = 15f
        const val COLOR_MODE_NORMAL = 0
        const val COLOR_MODE_INVERTED = 1

        const val CTA_MODE_SEE_ALL = 0
        const val CTA_MODE_RELOAD = 1
        const val CTA_MODE_CLOSE = 2

        private const val ROTATE_TO_DEGREES = 360f
        private const val PIVOT_X_VALUE = 0.5f
        private const val PIVOT_Y_VALUE = 0.5f
        private const val ROTATE_FROM_DEGREES = 0f
        private const val ROTATE_DURATION = 500L
    }
}
