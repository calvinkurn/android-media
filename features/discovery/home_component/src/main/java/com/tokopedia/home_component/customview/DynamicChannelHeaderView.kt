package com.tokopedia.home_component.customview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.home_component.util.getLink
import com.tokopedia.home_component.util.invertIfDarkMode
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

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.home_component_dynamic_channel_header, this)
        this.itemView = view
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
            handleHeaderExpiredTime(channel, stubCountDownView)
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
            channelTitle?.visibility = View.VISIBLE
            channelTitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).invertIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, R.color.Unify_N700).invertIfDarkMode(itemView?.context)
            )
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
            channelSubtitle?.setTextColor(
                    if (channel.channelHeader.textColor.isNotEmpty()) Color.parseColor(channel.channelHeader.textColor).invertIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, R.color.Unify_N700).invertIfDarkMode(itemView?.context)
            )
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
        if (isHasSeeMoreApplink(channel)) {
            seeAllButton = if (stubSeeAllButton is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButton)) {
                val stubSeeAllView = stubSeeAllButton.inflate()
                stubSeeAllView?.findViewById(R.id.see_all_button)
            } else {
                itemView?.findViewById(R.id.see_all_button)
            }

            handleSubtitlePosition(channelSubtitleName, channel, channelTitleContainer)

            if(channel.style == ChannelStyle.ChannelHome){
                seeAllButton?.setTextColor(ContextCompat.getColor(context, R.color.Unify_G500))
            } else if(channel.style == ChannelStyle.ChannelOS){
                seeAllButton?.setTypeface(null, Typeface.NORMAL)
                seeAllButton?.setTextColor(ContextCompat.getColor(context, R.color.Unify_P600))
            }

            seeAllButton?.show()
            seeAllButton?.setOnClickListener {
                listener?.onSeeAllClick(channel.channelHeader.getLink())
            }
        } else {
            seeAllButton?.hide()
        }
    }

    private fun handleSubtitlePosition(channelSubtitleName: String?, channel: ChannelModel, channelTitleContainer: ConstraintLayout?) {
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

    private fun handleBackImage(channel: ChannelModel, stubSeeAllButtonUnify: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * Show unify button of see more button for dc sprint if back image is not empty
         */
        if (channel.channelHeader.backImage.isNotBlank()) {
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

    private fun handleHeaderExpiredTime(channel: ChannelModel, stubCountDownView: View?) {
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
    }

    private fun handleBackgroundColor(channel: ChannelModel, titleContainer: ConstraintLayout, stubSeeAllButton: View?, stubSeeAllButtonUnify: View?) {
        if (channel.channelHeader.backColor.isNotEmpty()) {
            stubSeeAllButton?.gone()
            stubSeeAllButtonUnify?.gone()
            titleContainer.setBackgroundColor(Color.parseColor(channel.channelHeader.backColor))

            titleContainer.setPadding(
                    titleContainer.paddingLeft,
                    convertDpToPixel(10f, titleContainer.context),
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
}