package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop_widget.common.uimodel.DynamicHeaderUiModel
import com.tokopedia.shop_widget.common.util.DateHelper
import com.tokopedia.shop_widget.common.util.StatusCampaign
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.Calendar
import java.util.Date

class ShopCampaignDynamicHeaderCustomView : FrameLayout {

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
        val view = LayoutInflater.from(context).inflate(R.layout.shop_campaign_layout_dynamic_header_custom_view, this)
        this.itemView = view
    }

    fun setModel(model: DynamicHeaderUiModel, listener: HeaderCustomViewListener? = null, headerTextColor: Int) {
        this.listener = listener
        handleHeaderComponent(model, headerTextColor)
    }

    private fun handleHeaderComponent(model: DynamicHeaderUiModel, headerTextColor: Int) {
        setupUi()
        handleTitle(model.title, headerTextColor)
        handleSubtitle(model.subTitle, model.statusCampaign, model.endDate, headerTextColor)
        handleSeeAllAppLink(model.ctaText, model.ctaTextLink, headerTextColor)
    }

    private fun setupUi() {
        headerContainer = itemView?.findViewById(R.id.header_container)
        tusCountDown = itemView?.findViewById(R.id.tus_count_down)
        tpTitle = itemView?.findViewById(R.id.tp_title)
        tpSubtitle = itemView?.findViewById(R.id.tp_subtitle)
        tpSeeAll = itemView?.findViewById(R.id.tp_see_all)
    }

    private fun handleTitle(title: String, headerTextColor: Int) {
        if (title.isNotBlank()) {
            headerContainer?.show()
            tpTitle?.text = title
            tpTitle?.setTextColor(headerTextColor)
            tpTitle?.show()
        } else {
            headerContainer?.gone()
        }
    }

    private fun handleSubtitle(subtitle: String, statusCampaign: String, endDate: String, headerTextColor: Int) {
        if (subtitle.isNotBlank()) {
            tpSubtitle?.text = subtitle
            tpSubtitle?.setTextColor(headerTextColor)
            handleCountDownTimer(statusCampaign, endDate)
        } else {
            tusCountDown?.gone()
            tpSubtitle?.gone()
        }
    }

    private fun handleSeeAllAppLink(ctaText: String, ctaTextLink: String, headerTextColor: Int) {
        if (ctaTextLink.isNotBlank()) {
            tpSeeAll?.text = if (ctaText.isNotBlank()) {
                ctaText
            } else {
                itemView?.context?.getString(R.string.thematic_widget_see_all)
            }
            tpSeeAll?.setTextColor(headerTextColor)
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
            tusCountDown?.apply {
                isShowClockIcon = false
                timerFormat = TimerUnifySingle.FORMAT_AUTO
                timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
                onFinish = {
                    listener?.onTimerFinish()
                }
            }
            checkStatusCampaign(statusCampaign, endDate)
        } catch (e: Throwable) {
            tusCountDown?.gone()
            tpSubtitle?.gone()
        }
    }

    private fun checkStatusCampaign(statusCampaign: String, endDate: String) {
        when {
            isStatusCampaignOngoing(statusCampaign) -> setStatusCampaignOngoing(endDate)
            else -> setStatusCampaignFinished()
        }
    }

    private fun setStatusCampaignFinished() {
        tusCountDown?.gone()
        tpSubtitle?.gone()
    }

    private fun setStatusCampaignOngoing(endDate: String) {
        val calendar = Calendar.getInstance()
        val actualEndDate = DateHelper.getDateFromString(endDate).time
        calendar.time = Date(actualEndDate)
        tusCountDown?.targetDate = calendar
    }

    private fun isStatusCampaignOngoing(statusCampaign: String): Boolean {
        return statusCampaign.equals(StatusCampaign.ONGOING.statusCampaign, true)
    }

    interface HeaderCustomViewListener {
        fun onSeeAllClick(appLink: String)
        fun onTimerFinish()
    }
}
