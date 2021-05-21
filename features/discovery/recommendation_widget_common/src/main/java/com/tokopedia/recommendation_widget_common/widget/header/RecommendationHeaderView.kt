package com.tokopedia.recommendation_widget_common.widget.header

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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.DateHelper
import com.tokopedia.recommendation_widget_common.viewutil.convertDpToPixel
import com.tokopedia.recommendation_widget_common.viewutil.invertIfDarkMode
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class RecommendationHeaderView: FrameLayout {
    private var itemView: View?

    private var listener: RecommendationHeaderListener? = null

    var countDownView: TimerUnifySingle? = null
    var seeAllButton: TextView? = null
    var channelTitle: Typography? = null
    var seeAllButtonUnify: UnifyButton? = null
    var channelSubtitle: TextView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_widget_recommendation_header, this)
        this.itemView = view
    }

    fun bindData(data: RecommendationWidget, listener: RecommendationHeaderListener) {
        this.listener = listener
        handleHeaderComponent(data)
    }

    private fun handleHeaderComponent(data: RecommendationWidget) {
        val channelTitleContainer: ConstraintLayout? = itemView?.findViewById(R.id.channel_title_container)
        val stubChannelTitle: View? = itemView?.findViewById(R.id.channel_title)
        val stubCountDownView: View? = itemView?.findViewById(R.id.count_down)
        val stubSeeAllButton: View? = itemView?.findViewById(R.id.see_all_button)
        val stubSeeAllButtonUnify: View? = itemView?.findViewById(R.id.see_all_button_unify)
        val stubChannelSubtitle: View? = itemView?.findViewById(R.id.channel_subtitle)
        channelTitleContainer?.let {
            handleTitle(data.title, channelTitleContainer, stubChannelTitle, data)
            handleSubtitle(data.subtitle, stubChannelSubtitle, data)
            handleSeeAllApplink(data, stubSeeAllButton, data.subtitle, channelTitleContainer)
            handleBackImage(data, stubSeeAllButtonUnify, data.subtitle, channelTitleContainer)
            handleHeaderExpiredTime(data, stubCountDownView)
            handleBackgroundColor(data, it, stubSeeAllButton, stubSeeAllButtonUnify)
        }
    }

    private fun handleTitle(channelHeaderName: String?, channelTitleContainer: ConstraintLayout, stubChannelTitle: View?, data: RecommendationWidget) {
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
                    if (data.titleColor.isNotEmpty()) Color.parseColor(data.titleColor).invertIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, R.color.Unify_N700).invertIfDarkMode(itemView?.context)
            )
        } else {
            channelTitleContainer.visibility = View.GONE
        }
    }

    private fun handleSubtitle(channelSubtitleName: String?, stubChannelSubtitle: View?, data: RecommendationWidget) {
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
                    if (data.titleColor.isNotEmpty()) Color.parseColor(data.titleColor).invertIfDarkMode(itemView?.context)
                    else ContextCompat.getColor(context, R.color.Unify_N700).invertIfDarkMode(itemView?.context)
            )
        } else {
            channelSubtitle?.visibility = View.GONE
        }
    }

    private fun handleSeeAllApplink(data: RecommendationWidget, stubSeeAllButton: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * Only show `see all` button when it is exist
         * Don't show `see all` button on dynamic channel mix carousel
         */
        if (isHasSeeMoreApplink(data)) {
            seeAllButton = if (stubSeeAllButton is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButton)) {
                val stubSeeAllView = stubSeeAllButton.inflate()
                stubSeeAllView?.findViewById(R.id.see_all_button)
            } else {
                itemView?.findViewById(R.id.see_all_button)
            }

            handleSubtitlePosition(channelSubtitleName, data, channelTitleContainer)
            seeAllButton?.setTextColor(ContextCompat.getColor(context, R.color.Unify_G500))

            seeAllButton?.show()
            seeAllButton?.setOnClickListener {
                listener?.onSeeAllClick(data.seeMoreAppLink)
            }
        } else {
            seeAllButton?.hide()
        }
    }

    private fun handleSubtitlePosition(channelSubtitleName: String?, data: RecommendationWidget, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * `see all` button align to subtitle and countdown timer
         */
        if (channelSubtitleName?.isEmpty() != false && !hasExpiredTime(data)) {
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

    private fun handleBackImage(data: RecommendationWidget, stubSeeAllButtonUnify: View?, channelSubtitleName: String?, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * Show unify button of see more button for dc sprint if back image is not empty
         */
        if (data.headerBackImage.isNotBlank()) {
            seeAllButtonUnify = if (stubSeeAllButtonUnify is ViewStub &&
                    !isViewStubHasBeenInflated(stubSeeAllButtonUnify)) {
                val stubSeeAllButtonView = stubSeeAllButtonUnify.inflate()
                stubSeeAllButtonView?.findViewById(R.id.see_all_button_unify)
            } else {
                itemView?.findViewById(R.id.see_all_button_unify)
            }

            handleUnifySeeAllButton(channelSubtitleName, data, channelTitleContainer)
        }
    }

    private fun handleUnifySeeAllButton(channelSubtitleName: String?, data: RecommendationWidget, channelTitleContainer: ConstraintLayout?) {
        /**
         * Requirement:
         * `see all unify` button align to subtitle and countdown timer
         */
        if (channelSubtitleName?.isEmpty() != false && !hasExpiredTime(data)) {
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

    private fun handleHeaderExpiredTime(data: RecommendationWidget, stubCountDownView: View?) {
        /**
         * Requirement:
         * Only show countDownView when expired time exist
         * Don't start countDownView when it is expired from backend (possibly caused infinite refresh)
         *  since onCountDownFinished would getting called and refresh home
         */
        if (hasExpiredTime(data)) {
            countDownView = if (stubCountDownView is ViewStub &&
                    !isViewStubHasBeenInflated(stubCountDownView)) {
                val inflatedStubCountDownView = stubCountDownView.inflate()
                inflatedStubCountDownView.findViewById(R.id.count_down)
            } else {
                itemView?.findViewById(R.id.count_down)
            }

            val expiredTime = DateHelper.getExpiredTime(data.expiredTime)
            if (!DateHelper.isExpired(data.recommendationConfig.serverTimeOffset, expiredTime)) {
                countDownView?.run {
                    timerVariant = if(data.headerBackColor.isNotEmpty()){
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE

                    // calculate date diff
                    targetDate = Calendar.getInstance().apply {
                        val currentDate = Date()
                        val currentMillisecond: Long = currentDate.time + data.recommendationConfig.serverTimeOffset
                        val timeDiff = expiredTime.time - currentMillisecond
                        add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                        add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                        add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                    }
                    onFinish = {
                        listener?.onChannelExpired(data)
                    }

                }
            }
        } else {
            countDownView?.let {
                it.visibility = View.GONE
            }
        }
    }

    private fun handleBackgroundColor(data: RecommendationWidget, titleContainer: ConstraintLayout, stubSeeAllButton: View?, stubSeeAllButtonUnify: View?) {
        if (data.headerBackColor.isNotEmpty()) {
            stubSeeAllButton?.gone()
            stubSeeAllButtonUnify?.gone()
            titleContainer.setBackgroundColor(Color.parseColor(data.headerBackColor))

            titleContainer.setPadding(
                    titleContainer.paddingLeft,
                    convertDpToPixel(10f, titleContainer.context),
                    titleContainer.paddingRight,
                    titleContainer.paddingBottom)
        }

    }

    fun isHasSeeMoreApplink(data: RecommendationWidget): Boolean {
        return data.seeMoreAppLink.isNotEmpty()
    }

    private fun hasExpiredTime(data: RecommendationWidget): Boolean {
        return !TextUtils.isEmpty(data.expiredTime)
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }
}