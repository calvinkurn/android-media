package com.tokopedia.topchat.chatlist.view.widget

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.topchat.common.TopChatUrlConstant.FIX_IMAGE_URL
import com.tokopedia.topchat.common.TopChatUrlConstant.MAINTAIN_IMAGE_URL
import com.tokopedia.topchat.common.TopChatUrlConstant.TOPED_IMAGE_URL
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt.eventClickOperationalInsightCta
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt.eventClickShopPerformanceOperationalInsightBottomSheet
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt.eventViewOperationalInsightBottomSheet
import com.tokopedia.topchat.common.util.Utils.getOperationalInsightStateReport
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class OperationalInsightBottomSheet(
    private var ticker: ShopChatTicker,
    private var shopId: String
): BottomSheetUnify() {

    private var childView: View? = null

    init {
        showKnob = true
        showCloseIcon = false
        showHeader = false
        isHideable = true
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
        initUrlShopPerformance()
        initCtaButton()
        initTopedIcon()
        trackBottomSheet()
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

    private fun getIconSummary(): String {
        return when(ticker.isMaintain) {
            true -> MAINTAIN_IMAGE_URL
            false -> FIX_IMAGE_URL
            else -> ""
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
        val textActualReplyRate = "${ticker.data?.chatReplied.toString().removeSuffix(SUFFIX_FLOAT)}%"
        tvActualReplyRate?.let {
            it.text = textActualReplyRate
            val replyRateColor = getActualReplyRateTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualReplyRateTextColor(): Int? {
        try {
            if (isDarkMode()) {
                ticker.colorLight?.chatRepliedIndicatorLight?.let {
                    return Color.parseColor(it)
                }
            } else {
                ticker.colorDark?.chatRepliedIndicatorDark?.let {
                    return Color.parseColor(it)
                }
            }
        } catch (ignored: Throwable) {}
        return null
    }

    private fun initActualReplySpeed() {
        val tvActualReplySpeed: Typography? = childView?.findViewById(R.id.tv_actual_chat_reply_speed)
        val textActualReplySpeed = "${ticker.data?.chatSpeed.toString().removeSuffix(SUFFIX_FLOAT)} menit"
        tvActualReplySpeed?.let {
            it.text = textActualReplySpeed
            val replyRateColor = getActualReplySpeedTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualReplySpeedTextColor(): Int? {
        try {
            if (isDarkMode()) {
                ticker.colorLight?.chatSpeedIndicatorLight?.let {
                    return Color.parseColor(it)
                }
            } else {
                ticker.colorDark?.chatSpeedIndicatorDark?.let {
                    return Color.parseColor(it)
                }
            }
        } catch (ignored: Throwable) {}
        return null
    }

    private fun initActualDiscussionReplyRate() {
        val tvActualReplyRate: Typography? = childView?.findViewById(R.id.tv_actual_chat_and_discussion_reply_rate)
        val textActualReplyRate = "${ticker.data?.discussionReplied.toString().removeSuffix(SUFFIX_FLOAT)}%"
        tvActualReplyRate?.let {
            it.text = textActualReplyRate
            val replyRateColor = getActualDiscussionReplyRateTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualDiscussionReplyRateTextColor(): Int? {
        try {
            if (isDarkMode()) {
                ticker.colorLight?.discussionRepliedIndicatorLight?.let {
                    return Color.parseColor(it)
                }
            } else {
                ticker.colorDark?.discussionRepliedIndicatorDark?.let {
                    return Color.parseColor(it)
                }
            }
        } catch (ignored: Throwable) {}
        return null
    }

    private fun initActualDiscussionReplySpeed() {
        val tvActualReplySpeed: Typography? = childView?.findViewById(R.id.tv_actual_chat_and_discussion_reply_speed)
        val textActualReplySpeed = "${ticker.data?.discussionSpeed.toString().removeSuffix(SUFFIX_FLOAT)} menit"
        tvActualReplySpeed?.let {
            it.text = textActualReplySpeed
            val replyRateColor = getActualDiscussionReplySpeedTextColor()
            replyRateColor?.let { color ->
                it.setTextColor(color)
            }
        }
    }

    private fun getActualDiscussionReplySpeedTextColor(): Int? {
        try {
            if (isDarkMode()) {
                ticker.colorLight?.discussionSpeedIndicatorLight?.let {
                    return Color.parseColor(it)
                }
            } else {
                ticker.colorDark?.discussionSpeedIndicatorDark?.let {
                    return Color.parseColor(it)
                }
            }
        } catch (ignored: Throwable) {}
        return null
    }

    private fun isDarkMode(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }
    
    private fun initTarget() {
        val tvReplyRateTarget: Typography? = childView?.findViewById(R.id.tv_target_rate_reply)
        val replyRateTarget = ticker.target?.chatRepliedTarget.toIntSafely()
        val textReplyRate = "${ticker.target?.chatRepliedTarget.toString().removeSuffix(SUFFIX_FLOAT)}%"
        val textReplyRateTarget = if (replyRateTarget < LIMIT) {
            ">$textReplyRate"
        } else {
            textReplyRate
        }
        tvReplyRateTarget?.let {
            it.text = textReplyRateTarget
        }

        val tvReplyRateSpeed: Typography? = childView?.findViewById(R.id.tv_target_rate_speed)
        val textReplyRateSpeed = "<${ticker.target?.chatSpeedTarget.toString().removeSuffix(SUFFIX_FLOAT)} menit"
        tvReplyRateSpeed?.let {
            it.text = textReplyRateSpeed
        }
    }

    private fun initUrlShopPerformance() {
        val spannablePerformanceShop = createSpannableWithLink(
            getString(R.string.topchat_operational_insight_data_ninety_days))
        val tvOperationalInsightShopPerformance: Typography? = childView?.findViewById(
            R.id.tv_operational_insight_shop_performance)
        tvOperationalInsightShopPerformance?.isClickable = true
        tvOperationalInsightShopPerformance?.linksClickable = true
        tvOperationalInsightShopPerformance?.movementMethod = LinkMovementMethod.getInstance()
        tvOperationalInsightShopPerformance?.text = spannablePerformanceShop
    }

    private fun initCtaButton() {
        val ctaOperationalInsight: UnifyButton? = childView?.findViewById(R.id.btn_visit_operational_insight)
        ctaOperationalInsight?.setOnClickListener {
            goToOperationalInsightPage()
        }
    }

    private fun initTopedIcon() {
        val topedImage: ImageUnify? = childView?.findViewById(R.id.icon_toped_operational_insight)
        topedImage?.loadImage(TOPED_IMAGE_URL)
    }

    private fun createSpannableWithLink(completeString: String): SpannableString {
        val spannableString = SpannableString(completeString)
        try {
            val startPosition = completeString.indexOf(getPerformanceShopText())
            val endPosition = completeString.lastIndexOf(getPerformanceShopText()) +
                    getPerformanceShopText().length
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    goToShopScorePage()
                }
                override fun updateDrawState(drawState: TextPaint) {
                    super.updateDrawState(drawState)
                    drawState.isUnderlineText = false
                    drawState.color = MethodChecker.getColor(
                        context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                }
            }
            spannableString.setSpan(
                clickableSpan,
                startPosition,
                endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        } catch (ignored: Throwable) {}
        return spannableString
    }

    private fun getPerformanceShopText() : String {
        return getString(R.string.topchat_operational_insight_performance_shop)
    }

    private fun goToOperationalInsightPage() {
        context?.let {
            if (!ticker.operationalApplink.isNullOrEmpty()) {
                trackerOperationalInsightCta()
                val intent = RouteManager.getIntent(it, ticker.operationalApplink)
                startActivity(intent)
            }
        }
    }

    private fun goToShopScorePage() {
        context?.let {
            if (!ticker.shopScoreApplink.isNullOrEmpty()) {
                trackShopScore()
                val intent = RouteManager.getIntent(it, ticker.shopScoreApplink)
                startActivity(intent)
            }
        }
    }

    private fun trackBottomSheet() {
        eventViewOperationalInsightBottomSheet(
            shopId = shopId,
            stateReport = getOperationalInsightStateReport(ticker.isMaintain),
            replyChatRate = ticker.data?.chatReplied.toString().removeSuffix(SUFFIX_FLOAT),
            targetReplyChatRate = ticker.target?.chatRepliedTarget.toString().removeSuffix(SUFFIX_FLOAT),
            replyChatSpeed = ticker.data?.chatSpeed.toString().removeSuffix(SUFFIX_FLOAT),
            targetReplyChatSpeed = ticker.target?.chatSpeedTarget.toString().removeSuffix(SUFFIX_FLOAT),
        )
    }

    private fun trackerOperationalInsightCta() {
        eventClickOperationalInsightCta(
            shopId = shopId,
            stateReport = getOperationalInsightStateReport(ticker.isMaintain),
            replyChatRate = ticker.data?.chatReplied.toString().removeSuffix(SUFFIX_FLOAT),
            targetReplyChatRate = ticker.target?.chatRepliedTarget.toString().removeSuffix(SUFFIX_FLOAT),
            replyChatSpeed = ticker.data?.chatSpeed.toString().removeSuffix(SUFFIX_FLOAT),
            targetReplyChatSpeed = ticker.target?.chatSpeedTarget.toString().removeSuffix(SUFFIX_FLOAT),
        )
    }

    private fun trackShopScore() {
        eventClickShopPerformanceOperationalInsightBottomSheet(
            shopId = shopId,
            stateReport = getOperationalInsightStateReport(ticker.isMaintain),
            replyChatRate = ticker.data?.chatReplied.toString().removeSuffix(SUFFIX_FLOAT),
            targetReplyChatRate = ticker.target?.chatRepliedTarget.toString().removeSuffix(SUFFIX_FLOAT),
            replyChatSpeed = ticker.data?.chatSpeed.toString().removeSuffix(SUFFIX_FLOAT),
            targetReplyChatSpeed = ticker.target?.chatSpeedTarget.toString().removeSuffix(SUFFIX_FLOAT),
        )
    }

    companion object {
        private const val LIMIT = 100
        private const val SUFFIX_FLOAT = ".0"
    }
}