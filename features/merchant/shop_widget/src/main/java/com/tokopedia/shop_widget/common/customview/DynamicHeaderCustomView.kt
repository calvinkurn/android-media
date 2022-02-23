package com.tokopedia.shop_widget.common.customview

import android.view.LayoutInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.common.uimodel.DynamicHeaderUiModel
import com.tokopedia.shop_widget.common.util.DateHelper
import com.tokopedia.shop_widget.common.util.StatusCampaign
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import java.util.concurrent.TimeUnit


class DynamicHeaderCustomView: FrameLayout {

    companion object {
        private const val Hour24 = 24
        private const val INVALID_TIMER_COUNTER = 0
    }

    private var listener: HeaderCustomViewListener? = null
    private var itemView: View?
    private var headerContainer: ConstraintLayout? = null
    private var tusCountDown: TimerUnifySingle? = null
    private var tpSeeAll: Typography? = null
    private var tpTitle: Typography? = null
    private var tpSubtitle: Typography? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_header_custom_view, this)
        this.itemView = view
    }

    fun setModel(model: DynamicHeaderUiModel, listener: HeaderCustomViewListener? = null) {
        this.listener = listener
        handleHeaderComponent(model)
    }

    private fun handleHeaderComponent(model: DynamicHeaderUiModel) {
        setupUi()
        handleTitle(model.title)
        handleSubtitle(model.subTitle, model.statusCampaign, model.endDate, model.timerCounter)
        handleSeeAllAppLink(model.ctaText, model.ctaTextLink)
    }

    private fun setupUi() {
        headerContainer = itemView?.findViewById(R.id.header_container)
        tusCountDown = itemView?.findViewById(R.id.tus_count_down)
        tpTitle = itemView?.findViewById(R.id.tp_title)
        tpSubtitle = itemView?.findViewById(R.id.tp_subtitle)
        tpSeeAll =  itemView?.findViewById(R.id.tp_see_all)
    }

    private fun handleTitle(title: String) {
        if (title.isNotBlank()) {
            headerContainer?.show()
            tpTitle?.text = title
            tpTitle?.show()
        } else {
            headerContainer?.gone()
        }
    }

    private fun handleSubtitle(subtitle: String, statusCampaign: String, endDate: String, timerCounter: String) {
        if (subtitle.isNotBlank() && timerCounter.toLongOrZero() > INVALID_TIMER_COUNTER) {
            tpSubtitle?.text = subtitle
            handleCountDownTimer(statusCampaign, endDate)
        } else {
            tusCountDown?.gone()
            tpSubtitle?.gone()
        }
    }

    private fun handleSeeAllAppLink(ctaText: String, ctaTextLink: String) {
        if (ctaTextLink.isNotBlank()) {
            tpSeeAll?.text = if (ctaText.isNotBlank()) {
                ctaText
            } else {
                itemView?.context?.getString(R.string.thematic_widget_see_all)
            }
            tpSeeAll?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            tpSeeAll?.setOnClickListener {
                listener?.onSeeAllClick(ctaTextLink)
            }
            tpSeeAll?.show()
        } else {
            tpSeeAll?.hide()
        }
    }

    private fun handleCountDownTimer(statusCampaign: String, endDate: String) {
        try {
            tusCountDown?.isShowClockIcon = false
            checkStatusCampaign( statusCampaign, endDate)
            tusCountDown?.onFinish = {
                listener?.onTimerFinish()
            }
        } catch (e: Throwable) {
            tusCountDown?.gone()
            tpSubtitle?.gone()
        }
    }

    private fun checkStatusCampaign(statusCampaign: String, endDate: String) {
        when {
            isStatusCampaignOngoing(statusCampaign) -> checkStatusCampaignOngoing(endDate)
            else -> checkStatusCampaignFinished()
        }
    }

    private fun checkStatusCampaignFinished() {
        tusCountDown?.gone()
        tpSubtitle?.gone()
    }

    private fun checkStatusCampaignOngoing(endDate: String) {
        val calendar = Calendar.getInstance()
        val endDateMillis = DateHelper.getDateFromString(endDate).time
        val currentMillis = System.currentTimeMillis()
        val isMoreOrEqualThan1Day = getDateHours(endDateMillis - currentMillis) >= Hour24
        if (isMoreOrEqualThan1Day) {
            tusCountDown?.timerFormat = TimerUnifySingle.FORMAT_DAY
        } else {
            tusCountDown?.timerFormat = TimerUnifySingle.VARIANT_ALTERNATE
        }
        calendar.time = Date(endDateMillis)
        tusCountDown?.targetDate = calendar
    }

    private fun isStatusCampaignOngoing(statusCampaign: String): Boolean {
        return statusCampaign.equals(StatusCampaign.ONGOING.statusCampaign, true)
    }

    private fun getDateHours(millis: Long): Long {
        return TimeUnit.HOURS.convert(millis, TimeUnit.MILLISECONDS)
    }

    interface HeaderCustomViewListener {
        fun onSeeAllClick(appLink: String)
        fun onTimerFinish()
    }
}