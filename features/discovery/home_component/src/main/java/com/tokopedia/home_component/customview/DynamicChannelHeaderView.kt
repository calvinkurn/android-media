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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class DynamicChannelHeaderView: FrameLayout {
    private var itemView: View?

    private var listener: HeaderListener? = null

    var countDownView: TimerUnifySingle? = null
    var seeAllButton: TextView? = null
    var channelTitle: Typography? = null
    var seeAllButtonUnify: UnifyButton? = null
    var channelSubtitle: TextView? = null

    var seeAllButtonRevamp: IconUnify? = null
    var seeAllButtonRevampContainer: LinearLayout? = null
    var seeAllButtonMode: Int = MODE_NORMAL

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initHeaderWithAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHeaderWithAttrs(attrs)
    }

    init {
        val layout = if(HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant())
            R.layout.home_component_dynamic_channel_header_revamp
        else R.layout.home_component_dynamic_channel_header
        val view = LayoutInflater.from(context).inflate(layout, this)
        this.itemView = view
    }

    private fun initHeaderWithAttrs(attrs: AttributeSet?) {
        seeAllButtonMode = context.obtainStyledAttributes(attrs, R.styleable.DynamicChannelHeaderRevampView).getInt(0, MODE_NORMAL)
    }

    fun setChannel(channelModel: ChannelModel, listener: HeaderListener) {
        this.listener = listener
        handleHeaderComponent(channelModel)
    }

    private fun handleHeaderComponent(
            channel: ChannelModel) {
        val channelTitleContainer: ConstraintLayout? = itemView?.findViewById(R.id.channel_title_container)
        val stubChannelTitle: View? = itemView?.findViewById(R.id.channel_title)
        val stubCountDownView: View? = itemView?.findViewById(R.id.count_down)
        val stubSeeAllButton: View? = itemView?.findViewById(R.id.see_all_button)
        val stubSeeAllButtonUnify: View? = itemView?.findViewById(R.id.see_all_button_unify)
        val stubChannelSubtitle: View? = itemView?.findViewById(R.id.channel_subtitle)
        channelTitleContainer?.let {
            handleTitle(channel.channelHeader.name, channelTitleContainer, stubChannelTitle, channel)
            handleSubtitle(channel.channelHeader.subtitle, stubChannelSubtitle, channel)
            handleSeeAllApplink(channel, stubSeeAllButton, channel.channelHeader.subtitle, channelTitleContainer)
            handleBackImage(channel, stubSeeAllButtonUnify, channel.channelHeader.subtitle, channelTitleContainer)
            handleHeaderExpiredTime(channel, stubCountDownView, channelTitleContainer)
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
                itemView?.findViewById(R.id.channel_title)
            }
            channelTitle?.text = channelHeaderName
            channelTitle?.gravity = Gravity.CENTER_VERTICAL
            channelTitle?.visibility = View.VISIBLE
            if(HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant() && channel.style == ChannelStyle.ChannelHome) {
                channelTitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).staticIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
            } else {
                channelTitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).invertIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700).invertIfDarkMode(itemView?.context)
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
                itemView?.findViewById(R.id.channel_subtitle)
            }
            channelSubtitle?.text = channelSubtitleName
            channelSubtitle?.visibility = View.VISIBLE
            if(HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant()) {
                channelSubtitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).staticIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
            } else {
                channelSubtitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).invertIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700).invertIfDarkMode(itemView?.context)
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
         */
        if (isHasSeeMoreApplink(channel)) {
            if(HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant() && channel.style == ChannelStyle.ChannelHome) {
                if (stubSeeAllButton is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButton)) {
                    stubSeeAllButton.inflate()?.initializeHeader()
                } else {
                    itemView?.initializeHeader()
                }
                setHeaderRevampColor()

                seeAllButton?.hide()
                seeAllButtonRevampContainer?.show()
                seeAllButtonRevampContainer?.setOnClickListener {
                    listener?.onSeeAllClick(channel.channelHeader.getLink())
                }
            } else {
                seeAllButton = if (stubSeeAllButton is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButton)) {
                    val stubSeeAllView = stubSeeAllButton.inflate()
                    stubSeeAllView?.findViewById(R.id.see_all_button)
                } else {
                    itemView?.findViewById(R.id.see_all_button)
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

    private fun View.initializeHeader() {
        seeAllButtonRevamp = findViewById(R.id.see_all_button_revamp)
        seeAllButtonRevampContainer = findViewById(R.id.see_all_revamp_border)
    }

    private fun setHeaderRevampColor() {
        when(seeAllButtonMode) {
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
                    newLightEnable = ContextCompat.getColor(context, com.tokopedia.home_component.R.color.home_dms_header_see_all_button_inverted),
                    newDarkEnable = ContextCompat.getColor(context, com.tokopedia.home_component.R.color.home_dms_header_see_all_button_inverted)
                )
            }
        }
    }

    private fun handleSeeAllPosition(channelSubtitleName: String?, channel: ChannelModel, channelTitleContainer: ConstraintLayout?) {
        if(HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant()) {
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
        if (channel.channelHeader.backImage.isNotBlank() && !HomeComponentRollenceController.isDynamicChannelHeaderUsingRollenceVariant()) {
            seeAllButtonUnify = if (stubSeeAllButtonUnify is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButtonUnify)) {
                val stubSeeAllButtonView = stubSeeAllButtonUnify.inflate()
                stubSeeAllButtonView?.findViewById(R.id.see_all_button_unify)
            } else {
                itemView?.findViewById(R.id.see_all_button_unify)
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
                itemView?.findViewById(R.id.count_down)
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

    fun isHasSeeMoreApplink(channel: ChannelModel): Boolean {
        return channel.channelHeader.getLink().isNotEmpty()
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

    companion object {
        private const val TITLE_TOP_PADDING = 15f
        const val MODE_NORMAL = 0
        const val MODE_INVERTED = 1
    }
}
