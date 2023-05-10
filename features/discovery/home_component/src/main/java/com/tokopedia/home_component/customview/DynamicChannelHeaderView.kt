package com.tokopedia.home_component.customview

import android.content.Context
import android.content.res.Configuration
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
    private var seeAllButtonRevamp: IconUnify? = null
    private var seeAllButtonRevampContainer: LinearLayout? = null
    private var reloadButton: IconUnify? = null
    private var reloadButtonContainer: LinearLayout? = null
    private val rotateAnimation = RotateAnimation(ROTATE_FROM_DEGREES, ROTATE_TO_DEGREES, Animation.RELATIVE_TO_SELF, PIVOT_X_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_Y_VALUE)
    private var headerMode: Int = MODE_NORMAL
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

        rotateAnimation.duration = ROTATE_DURATION
        rotateAnimation.interpolator = LinearInterpolator()
    }

    private fun initHeaderWithAttrs(attrs: AttributeSet?) {
        headerMode = context.obtainStyledAttributes(attrs, R.styleable.DynamicChannelHeaderRevampView).getInt(0, MODE_NORMAL)
    }

    fun setChannel(channelModel: ChannelModel, listener: HeaderListener, isReload: Boolean = false) {
        this.listener = listener
        handleHeaderComponent(channelModel, isReload)
    }

    private fun handleHeaderComponent(channel: ChannelModel, isReload: Boolean) {
        val stubChannelTitle: View? = itemView.findViewById(R.id.channel_title)
        val stubCountDownView: View? = itemView.findViewById(R.id.count_down)
        val stubSeeAllButton: View? = itemView.findViewById(R.id.see_all_button)
        val stubSeeAllButtonUnify: View? = itemView.findViewById(R.id.see_all_button_unify)
        val stubChannelSubtitle: View? = itemView.findViewById(R.id.channel_subtitle)
        val stubReloadButton: View? = itemView.findViewById(R.id.reload_button)
        channelHeaderContainer.let {
            handleTitle(channel.channelHeader.name, channelHeaderContainer, stubChannelTitle, channel)
            handleSubtitle(channel.channelHeader.subtitle, stubChannelSubtitle, channel)
            handleSeeAllApplink(channel, stubSeeAllButton, channel.channelHeader.subtitle, channelHeaderContainer, isReload)
            handleBackImage(channel, stubSeeAllButtonUnify, channel.channelHeader.subtitle, channelHeaderContainer)
            handleHeaderExpiredTime(channel, stubCountDownView, channelHeaderContainer)
            handleBackgroundColor(channel, it, stubSeeAllButton, stubSeeAllButtonUnify)
            handleReloadButton(channel, stubReloadButton, isReload)
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
                    if (headerMode == MODE_INVERTED) ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
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
                    if (headerMode == MODE_INVERTED) ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
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

    private fun handleSeeAllApplink(channel: ChannelModel, stubSeeAllButton: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?, isReload: Boolean) {
        /**
         * Requirement:
         * Only show `see all` button when it is exist
         */
        if (isHasSeeMoreApplink(channel) && !isReload) {
            if(isUsingHeaderRevamp && channel.style == ChannelStyle.ChannelHome) {
                if (stubSeeAllButton is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButton)) {
                    stubSeeAllButton.inflate()?.initializeSeeAll()
                } else {
                    itemView.initializeSeeAll()
                }
                setHeaderRevampCtaColor()

                seeAllButton?.hide()
                seeAllButtonRevampContainer?.show()
                seeAllButtonRevamp?.setOnClickListener {
                    listener?.onSeeAllClick(channel.channelHeader.getLink())
                }
            } else {
                seeAllButton = if (stubSeeAllButton is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButton)) {
                    val stubSeeAllView = stubSeeAllButton.inflate()
                    stubSeeAllView?.findViewById(R.id.see_all_button)
                } else {
                    itemView.findViewById(R.id.see_all_button)
                }

                if(channel.style == ChannelStyle.ChannelHome){
                    seeAllButton?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                } else if(channel.style == ChannelStyle.ChannelOS){
                    seeAllButton?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_P600))
                }

                seeAllButtonRevampContainer?.hide()
                seeAllButton?.show()
                seeAllButton?.setOnClickListener {
                    listener?.onSeeAllClick(channel.channelHeader.getLink())
                }
            }

            handleSeeAllPosition(channelSubtitleName, channel, channelTitleContainer)
        } else {
            seeAllButtonRevampContainer?.hide()
            seeAllButton?.hide()
        }
    }

    private fun setHeaderRevampCtaColor() {
        when(headerMode) {
            MODE_NORMAL -> {
                (seeAllButtonRevampContainer?.background as? GradientDrawable)?.setColor(com.tokopedia.unifyprinciples.R.color.Unify_NN200)
                seeAllButtonRevamp?.setImage(
                    newIconId = IconUnify.CHEVRON_RIGHT,
                    newLightEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900),
                    newDarkEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
            }
            MODE_INVERTED -> {
                (seeAllButtonRevampContainer?.background as? GradientDrawable)?.setColor(com.tokopedia.home_component.R.color.home_dms_header_see_all_border_inverted)
                seeAllButtonRevamp?.setImage(
                    newIconId = IconUnify.CHEVRON_RIGHT,
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
            if (channelSubtitleName?.isEmpty() != false && !hasExpiredTime(channel)) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(channelTitleContainer)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, R.id.channel_title, ConstraintSet.BOTTOM, 0)
                constraintSet.applyTo(channelTitleContainer)
            } else {
                val constraintSet = ConstraintSet()
                constraintSet.clone(channelTitleContainer)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, R.id.count_down, ConstraintSet.BOTTOM, 0)
                constraintSet.applyTo(channelTitleContainer)
            }
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
                    timerVariant = if(channel.channelHeader.backColor.isNotEmpty()){
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE
                    channelTitleContainer.setPadding(channelTitleContainer.paddingLeft, channelTitleContainer.paddingTop, channelTitleContainer.paddingRight, 8f.toDpInt())
                    if(isUsingHeaderRevamp) anchorSubtitleToTimer()
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
            if(isUsingHeaderRevamp) anchorSubtitleToTitle()
            countDownView?.let {
                it.visibility = View.GONE
                channelTitleContainer.setPadding(channelTitleContainer.paddingLeft, channelTitleContainer.paddingTop, channelTitleContainer.paddingRight, 12f.toDpInt())
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

    private fun handleReloadButton(channel: ChannelModel, stubReloadButton: View?, isReload: Boolean) {
        if(isReload) {
            if (stubReloadButton is ViewStub &&
                !isViewStubHasBeenInflated(stubReloadButton)) {
                stubReloadButton.inflate()?.initializeReload()
            } else {
                itemView.initializeReload()
            }
            reloadButton?.setOnClickListener {
                it.startAnimation(rotateAnimation)
                listener?.onReloadClick(channel)
            }
        } else {
            reloadButtonContainer?.hide()
            reloadButton?.hide()
        }
    }

    fun hideTitleAndSubtitle() {
        channelTitle?.invisible()
        channelSubtitle?.invisible()
    }

    private fun isHasSeeMoreApplink(channel: ChannelModel): Boolean {
        return channel.channelHeader.getLink().isNotEmpty()
    }

    private fun View.initializeSeeAll() {
        seeAllButtonRevamp = findViewById(R.id.see_all_button_revamp)
        seeAllButtonRevampContainer = findViewById(R.id.see_all_revamp_border)
    }

    private fun View.initializeReload() {
        reloadButton = findViewById(R.id.reload_button)
        reloadButtonContainer = findViewById(R.id.reload_border)
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
        const val MODE_NORMAL = 0
        const val MODE_INVERTED = 1

        private const val ROTATE_TO_DEGREES = 360f
        private const val PIVOT_X_VALUE = 0.5f
        private const val PIVOT_Y_VALUE = 0.5f
        private const val ROTATE_FROM_DEGREES = 0f
        private const val ROTATE_DURATION = 500L
    }
}
