package com.tokopedia.topchat.chatlist.view.widget

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.text.DecimalFormat

class OperationalInsightBottomSheet(private var ticker: ShopChatTicker): BottomSheetUnify() {

    private var childView: View? = null

    init {
        showKnob = true
        showCloseIcon = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        childView = View.inflate(context, R.layout.bs_chat_operational_insight, null)
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDate()
        initPerformanceSummary()
        initActualPerformance()
        initTarget()
    }

    private fun initDate() {
        val datePerformanceWeekly: Typography? =
            childView?.findViewById(R.id.tv_chat_performance_weekly)
        val stringDatePerformanceWeekly = String.format(
            getString(R.string.topchat_operational_insight_weekly),
            ticker.date
        )
        val textDatePerformanceWeekly = HtmlCompat.fromHtml(
            stringDatePerformanceWeekly, HtmlCompat.FROM_HTML_MODE_LEGACY)
        datePerformanceWeekly?.text = textDatePerformanceWeekly
    }

    private fun initPerformanceSummary() {
        val layoutPerformanceSummary: ConstraintLayout? =
            childView?.findViewById(R.id.layout_chat_performance_summary)
        val background: Drawable? = layoutPerformanceSummary?.background
        background?.let {
            setLayoutPerformanceSettingBackground(it)
        }

        val tvSummary: Typography? = childView?.findViewById(R.id.tv_chat_performance_summary)
        val textSummary = getTextSummary()
        if (tvSummary != null && textSummary != null) {
            tvSummary.text = textSummary
        }

        val iconSummary: ImageUnify? = childView?.findViewById(R.id.icon_chat_performance_summary)
        iconSummary?.loadImage(getIconSummary())
    }

    private fun setLayoutPerformanceSettingBackground(drawable: Drawable) {
        if (drawable is GradientDrawable) {
            when (ticker.isMaintain) {
                true -> {
                    val maintainTickerColor = getMaintainTickerColors()
                    drawable.colors = maintainTickerColor
                }
                false-> {
                    val notMaintainTickerColor = getNotMaintainTickerColors()
                    drawable.colors = notMaintainTickerColor
                }
            }
        }
    }

    private fun getMaintainTickerColors(): IntArray {
        return intArrayOf(
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN50),
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_TN50)
        )
    }

    private fun getNotMaintainTickerColors(): IntArray {
        return intArrayOf(
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y100),
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y200)
        )
    }

    private fun getTextSummary(): Spanned? {
        return when(ticker.isMaintain) {
            true -> {
                HtmlCompat.fromHtml(
                    getString(R.string.topchat_operational_insight_ticker_maintain),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            false -> {
                HtmlCompat.fromHtml(
                    getString(R.string.topchat_operational_insight_ticker_fix),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            else -> null
        }
    }

    private fun getIconSummary(): Int {
        return when(ticker.isMaintain) {
            true -> R.drawable.ic_performance_chat_maintain
            false -> R.drawable.ic_performance_chat_fix
            else -> 0
        }
    }

    private fun initActualPerformance() {
        initActualReplyRate()
        initActualReplySpeed()
        initActualDiscussionReplyRate()
        initActualDiscussionReplySpeed()
    }

    private fun initActualReplyRate() {
        val tvActualReplyRate: Typography? = childView?.findViewById(R.id.tv_actual_chat_reply_rate)
        val textActualReplyRate = "${ticker.data?.chatReplied?.removeDecimal()}%"
        tvActualReplyRate?.let {
            it.text = textActualReplyRate
            val replyRateColor = getActualReplyRateTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualReplyRateTextColor(): Int? {
        if (isDarkMode()) {
            ticker.colorLight?.chatRepliedIndicatorLight?.let {
                return Color.parseColor(it)
            }
        } else {
            ticker.colorDark?.chatRepliedIndicatorDark?.let {
                return Color.parseColor(it)
            }
        }
        return null
    }

    private fun initActualReplySpeed() {
        val tvActualReplySpeed: Typography? = childView?.findViewById(R.id.tv_actual_chat_reply_speed)
        val textActualReplySpeed = "${ticker.data?.chatSpeed?.removeDecimal()} menit"
        tvActualReplySpeed?.let {
            it.text = textActualReplySpeed
            val replyRateColor = getActualReplySpeedTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualReplySpeedTextColor(): Int? {
        if (isDarkMode()) {
            ticker.colorLight?.chatSpeedIndicatorLight?.let {
                return Color.parseColor(it)
            }
        } else {
            ticker.colorDark?.chatSpeedIndicatorDark?.let {
                return Color.parseColor(it)
            }
        }
        return null
    }

    private fun initActualDiscussionReplyRate() {
        val tvActualReplyRate: Typography? = childView?.findViewById(R.id.tv_actual_chat_and_discussion_reply_rate)
        val textActualReplyRate = "${ticker.data?.discussionReplied?.removeDecimal()}%"
        tvActualReplyRate?.let {
            it.text = textActualReplyRate
            val replyRateColor = getActualDiscussionReplyRateTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualDiscussionReplyRateTextColor(): Int? {
        if (isDarkMode()) {
            ticker.colorLight?.discussionRepliedIndicatorLight?.let {
                return Color.parseColor(it)
            }
        } else {
            ticker.colorDark?.discussionRepliedIndicatorDark?.let {
                return Color.parseColor(it)
            }
        }
        return null
    }

    private fun initActualDiscussionReplySpeed() {
        val tvActualReplySpeed: Typography? = childView?.findViewById(R.id.tv_actual_chat_and_discussion_reply_speed)
        val textActualReplySpeed = "${ticker.data?.discussionSpeed?.removeDecimal()} menit"
        tvActualReplySpeed?.let {
            it.text = textActualReplySpeed
            val replyRateColor = getActualDiscussionReplySpeedTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualDiscussionReplySpeedTextColor(): Int? {
        if (isDarkMode()) {
            ticker.colorLight?.discussionSpeedIndicatorLight?.let {
                return Color.parseColor(it)
            }
        } else {
            ticker.colorDark?.discussionSpeedIndicatorDark?.let {
                return Color.parseColor(it)
            }
        }
        return null
    }

    private fun isDarkMode(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }
    
    private fun initTarget() {
        val tvReplyRateTarget: Typography? = childView?.findViewById(R.id.tv_target_rate_reply)
        val textReplyRateTarget = ">${ticker.target?.chatRepliedTarget?.removeDecimal()}%"
        tvReplyRateTarget?.let {
            it.text = textReplyRateTarget
        }

        val tvReplyRateSpeed: Typography? = childView?.findViewById(R.id.tv_target_rate_speed)
        val textReplyRateSpeed = "<${ticker.target?.chatSpeedTarget?.removeDecimal()} menit"
        tvReplyRateSpeed?.let {
            it.text = textReplyRateSpeed
        }
    }

    private fun Float.removeDecimal(): String {
        return DecimalFormat("#").format(this)
    }
}