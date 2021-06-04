package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import java.util.concurrent.TimeUnit

class CampaignRibbon @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    interface CampaignCountDownCallback {
        fun onOnGoingCampaignEnded(campaign: CampaignModular)
    }

    companion object {

        // campaign types
        const val NO_CAMPAIGN = 0
        const val FLASH_SALE = 1
        const val SLASH_PRICE = 2
        const val NPL = 3
        const val NEW_USER = 4
        const val THEMATIC_CAMPAIGN = 5

        // time unit
        private const val ONE_SECOND = 1000L
    }

    // upcoming components - structure type 1
    private var campaignRibbonType1View: View? = null
    private var campaignNameViews1: Typography? = null
    private var timerView1: TimerUnifySingle? = null
    private var regulatoryInfoLayout1: View? = null
    private var remindMeButton1: Typography? = null

    // ongoing components - structure type 2
    private var campaignRibbonType2View: View? = null
    private var campaignLogoView2: ImageView? = null
    private var campaignLogoContainerView2: FrameLayout? = null
    private var campaignNameView2: Typography? = null
    private var timerView2: TimerUnifySingle? = null
    private var stockWordingView2: Typography? = null
    private var stockBarView2: ProgressBarUnify? = null
    private var regulatoryInfoView2: Typography? = null

    // slash price / thematic only components - structure type 3
    private var campaignRibbonType3View: View? = null
    private var campaignLogoView3: ImageView? = null
    private var campaignLogoContainerView3: FrameLayout? = null
    private var campaignNameViews3: Typography? = null
    private var timerView3: TimerUnifySingle? = null
    private var endsInWordingView3: Typography? = null
    private var regulatoryInfo3: Typography? = null

    // listeners , callback properties
    private var callback: CampaignCountDownCallback? = null
    private var listener: DynamicProductDetailListener? = null
    private var trackDataModel: ComponentTrackDataModel? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(
                R.layout.widget_campaign_ribbon_layout,
                this,
                false)
        addView(rootView)
        collectViewReferences(rootView)
    }

    private fun collectViewReferences(rootView: View) {
        // TYPE 1 PROPERTIES
        campaignRibbonType1View = rootView.findViewById(R.id.campaign_ribbon_type_1)
        campaignNameViews1 = campaignRibbonType1View?.findViewById(R.id.tpg_campaign_name_s1)
        timerView1 = campaignRibbonType1View?.findViewById(R.id.tus_timer_view_s1)
        regulatoryInfoLayout1 = campaignRibbonType1View?.findViewById(R.id.regulatory_info_layout_s1)
        remindMeButton1 = campaignRibbonType1View?.findViewById(R.id.remind_me_button_s1)
        // TYPE 2 PROPERTIES
        campaignRibbonType2View = rootView.findViewById(R.id.campaign_ribbon_type_2)
        campaignLogoView2 = campaignRibbonType2View?.findViewById(R.id.iu_campaign_logo_s2)
        campaignLogoContainerView2 = campaignRibbonType2View?.findViewById(R.id.iu_campaign_logo_s2_container)
        campaignNameView2 = campaignRibbonType2View?.findViewById(R.id.tpg_campaign_name_s2)
        timerView2 = campaignRibbonType2View?.findViewById(R.id.tus_timer_view_s2)
        stockWordingView2 = campaignRibbonType2View?.findViewById(R.id.tgp_stock_wording_s2)
        stockBarView2 = campaignRibbonType2View?.findViewById(R.id.pbu_stock_bar_s2)
        regulatoryInfoView2 = campaignRibbonType2View?.findViewById(R.id.tgp_regulatory_info_s2)
        // TYPE 3 PROPERTIES
        campaignRibbonType3View = rootView.findViewById(R.id.campaign_ribbon_type_3)
        campaignLogoContainerView3 = rootView.findViewById(R.id.iu_campaign_logo_s3_container)
        campaignLogoView3 = campaignRibbonType3View?.findViewById(R.id.iu_campaign_logo_s3)
        campaignNameViews3 = campaignRibbonType3View?.findViewById(R.id.tpg_campaign_name_s3)
        timerView3 = campaignRibbonType3View?.findViewById(R.id.tus_timer_view_s3)
        endsInWordingView3 = campaignRibbonType3View?.findViewById(R.id.tpg_ends_in_s3)
        regulatoryInfo3 = campaignRibbonType3View?.findViewById(R.id.tgp_regulatory_info_s3)
    }

    fun setCampaignCountDownCallback(callback: CampaignCountDownCallback) {
        this.callback = callback
    }

    fun setDynamicProductDetailListener(listener: DynamicProductDetailListener) {
        this.listener = listener
    }

    fun setComponentTrackDataModel(trackDataModel: ComponentTrackDataModel) {
        this.trackDataModel = trackDataModel
    }

    fun renderOnGoingCampaign(onGoingData: ProductContentMainData) {
        this.show()
        when (onGoingData.campaign.campaignIdentifier) {
            NO_CAMPAIGN -> this.hide()
            FLASH_SALE, NEW_USER, NPL -> renderFlashSaleCampaignRibbon(onGoingData = onGoingData)
            SLASH_PRICE -> renderSlashPriceCampaignRibbon(onGoingData = onGoingData)
            THEMATIC_CAMPAIGN -> renderThematicCampaignRibbon(onGoingData = onGoingData)
        }
    }

    // FLASH SALE - use campaign ribbon structure type 2
    private fun renderFlashSaleCampaignRibbon(onGoingData: ProductContentMainData? = null) {
        onGoingData?.let {
            renderOnGoingCampaignRibbon(onGoingData)
            showCampaignRibbonType2()
        }
    }

    // SLASH PRICE - not eligible for thematic campaign - use campaign ribbon structure type 3
    private fun renderSlashPriceCampaignRibbon(onGoingData: ProductContentMainData) {
        val campaign = onGoingData.campaign
        if (onGoingData.thematicCampaign.campaignName.isEmpty()) {
            if (campaign.shouldShowRibbonCampaign) {
                // thematic data
                val thematicCampaign = onGoingData.thematicCampaign
                // render campaign name
                campaignNameViews3?.text = campaign.campaignTypeName
                // render campaign ribbon background
                val gradientDrawable = getGradientDrawableForBackGround(campaign.background, SLASH_PRICE)
                campaignRibbonType3View?.background = gradientDrawable
                // show count down wording
                endsInWordingView3?.show()
                // render ongoing count down timer
                renderOnGoingCountDownTimer(campaign = campaign, timerView = timerView3)
                // hide irrelevant views
                regulatoryInfo3?.hide()
                hideLogoView3()
                // show campaign ribbon type 3
                showCampaignRibbonType3()
            } else this.hide()
        } else {
            // if thematic have value, render thematic instead of slash price
            renderThematicCampaignRibbon(onGoingData)
        }
    }

    // THEMATIC ONLY - use campaign ribbon structure type 3
    private fun renderThematicCampaignRibbon(onGoingData: ProductContentMainData) {
        val thematicCampaign = onGoingData.thematicCampaign
        // render campaign ribbon background
        if (thematicCampaign.background.isNotBlank()) {
            campaignRibbonType3View?.background = getGradientDrawableForBackGround(thematicCampaign.background)
        }
        // render campaign logo
        if (thematicCampaign.icon.isNotBlank()) {
            campaignLogoView3?.loadImage(thematicCampaign.icon)
            showLogoView3()
        } else {
            hideLogoView3()
        }
        // render campaign name
        campaignNameViews3?.text = thematicCampaign.campaignName
        // hide irrelevant views
        regulatoryInfo3?.hide()
        endsInWordingView3?.hide()
        timerView3?.hide()
        // show campaign ribbon type 3
        showCampaignRibbonType3()
    }

    private fun hideLogoView3() {
        campaignLogoView3?.hide()
        campaignLogoContainerView3?.hide()
    }

    private fun showLogoView3() {
        campaignLogoView3?.show()
        campaignLogoContainerView3?.show()
    }

    // show upcoming structure
    private fun showCampaignRibbonType1() {
        campaignRibbonType1View?.show()
        campaignRibbonType2View?.hide()
        campaignRibbonType3View?.hide()
    }

    // show ongoing structure
    private fun showCampaignRibbonType2() {
        campaignRibbonType1View?.hide()
        campaignRibbonType2View?.show()
        campaignRibbonType3View?.hide()
    }

    // show thematic only, new user, slash price structure
    private fun showCampaignRibbonType3() {
        campaignRibbonType1View?.hide()
        campaignRibbonType2View?.hide()
        campaignRibbonType3View?.show()
    }

    // UPCOMING CAMPAIGN -  use campaign ribbon structure type 1
    fun renderUpComingCampaignRibbon(upcomingData: ProductNotifyMeDataModel?, upcomingIdentifier: String) {
        showCampaignRibbonType1()
        val gradientDrawable = if (upcomingIdentifier == ProductUpcomingTypeDef.UPCOMING_NPL) {
            ContextCompat.getDrawable(context, R.drawable.bg_gradient_default_blue)
        } else {
            ContextCompat.getDrawable(context, R.drawable.bg_gradient_default_red)
        }
        // render campaign ribbon background
        gradientDrawable?.run { campaignRibbonType1View?.background = gradientDrawable }
        // render campaign name
        val campaignTypeName = upcomingData?.upcomingNplData?.ribbonCopy ?: ""
        campaignNameViews1?.text = if (campaignTypeName.isNotEmpty()) campaignTypeName else context.getString(R.string.notify_me_title)
        // count down timer
        upcomingData?.let { data ->
            renderUpComingNplCountDownTimer(
                    data.startDate,
                    timerView1
            )
        }
        // update remind me button
        updateRemindMeButton(listener, upcomingData, upcomingIdentifier)
        // hide regulatory info
        regulatoryInfoLayout1?.hide()
    }

    private fun renderUpComingNplCountDownTimer(startDateData: String, timerView: TimerUnifySingle?) {
        try {
            val now = System.currentTimeMillis()
            val startTime = startDateData.toLongOrZero() * ONE_SECOND
            val startDate = Date(startTime)
            val calendar = Calendar.getInstance()
            calendar.time = startDate
            timerView?.targetDate = calendar
            timerView?.isShowClockIcon = false

            // less then 24 hours campaign period
            if (TimeUnit.MILLISECONDS.toDays(startDate.time - now) < 1) {
                timerView?.timerFormat = TimerUnifySingle.FORMAT_HOUR
                timerView?.onFinish = {
                    listener?.refreshPage()
                }
            } else {
                timerView?.timerFormat = TimerUnifySingle.FORMAT_DAY
            }
            timerView?.show()
        } catch (e: Throwable) {
            this.hide()
        }
    }

    private fun renderUpComingRemindMeButton(isOwner: Boolean,
                                             upComingData: ProductNotifyMeDataModel,
                                             remindMeButton: Typography?,
                                             upcomingIdentifier: String) {
        remindMeButton?.showWithCondition(!isOwner && upcomingIdentifier != ProductUpcomingTypeDef.UPCOMING_NPL)
        when (upComingData.notifyMe) {
            true -> {
                remindMeButton?.text = context.getString(R.string.notify_me_active)
                remindMeButton?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
                remindMeButton?.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_active)
            }
            false -> {
                remindMeButton?.text = context.getString(R.string.notify_me_inactive)
                remindMeButton?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
                remindMeButton?.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_inactive)
            }
        }
        remindMeButton?.setOnClickListener {
            listener?.onNotifyMeClicked(upComingData, this.trackDataModel
                    ?: ComponentTrackDataModel())
        }
    }

    fun updateRemindMeButton(listener: DynamicProductDetailListener?, upComingData: ProductNotifyMeDataModel?, upcomingIdentifier: String) {
        upComingData?.let {
            renderUpComingRemindMeButton(listener?.isOwner()
                    ?: false, upComingData, remindMeButton1, upcomingIdentifier)
        }
    }

    // ONGOING CAMPAIGN - use campaign ribbon structure type 2 -
    private fun renderOnGoingCampaignRibbon(onGoingData: ProductContentMainData) {
        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign
        // render campaign ribbon background
        val backGroundColorData = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else campaign.background
        val colorCount = backGroundColorData.split(",").size
        if (colorCount == 1) {
            campaignRibbonType2View?.setBackgroundColor(Color.parseColor(backGroundColorData))
        } else {
            val gradientDrawable = getGradientDrawableForBackGround(backGroundColorData)
            campaignRibbonType2View?.background = gradientDrawable
        }
        // render campaign logo
        if (thematicCampaign.icon.isNotBlank()) {
            campaignLogoView2?.loadImage(thematicCampaign.icon)
            showLogoView2()
        } else hideLogoView2()

        // render campaign name
        val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
        campaignNameView2?.text = campaignName
        // render ongoing count down
        timerView2?.let { renderOnGoingCountDownTimer(campaign = campaign, timerView = timerView2) }
        // render stock wording
        renderStockWording(stockWording = onGoingData.stockWording, stockTypography = stockWordingView2)
        // render stock bar
        renderStockBar(campaign.stockSoldPercentage, stockBarView2)
        // render regulatory info
        if (campaign.paymentInfoWording.isNotBlank()) {
            regulatoryInfoView2?.text = campaign.paymentInfoWording
            regulatoryInfoView2?.show()
        } else {
            regulatoryInfoView2?.hide()
        }
    }

    private fun hideLogoView2() {
        campaignLogoView2?.hide()
        campaignLogoContainerView2?.hide()
    }

    private fun showLogoView2() {
        campaignLogoView2?.show()
        campaignLogoContainerView2?.show()
    }

    private fun renderStockWording(stockWording: String, stockTypography: Typography?) {
        try {
            val styledStockWording = MethodChecker.fromHtml(stockWording)
            stockTypography?.text = styledStockWording
            stockTypography?.show()
        } catch (ex: Exception) {
            stockTypography?.hide()
        }
    }

    private fun renderStockBar(stockSoldPercentage: Int, stockProgressBar: ProgressBarUnify?) {
        // set track color
        stockProgressBar?.trackDrawable?.setColor(ContextCompat.getColor(context, R.color.product_detail_dms_stock_bar_track_color))
        // set progressbar color gradient, if using 1 color then set the same color amount
        val stockBarColor = ContextCompat.getColor(context, R.color.product_detail_dms_stock_bar_progress_color)
        stockProgressBar?.progressBarColor = intArrayOf(stockBarColor, stockBarColor)
        stockProgressBar?.setValue(stockSoldPercentage)
    }

    private fun renderOnGoingCountDownTimer(campaign: CampaignModular, timerView: TimerUnifySingle?) {
        try {
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            val now = System.currentTimeMillis()
            val endDate = dateFormat.parse(campaign.endDate)
            val calendar = Calendar.getInstance()
            calendar.time = endDate
            timerView?.targetDate = calendar
            timerView?.isShowClockIcon = false

            // less then 24 hours campaign period
            if (TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1) {
                timerView?.timerFormat = TimerUnifySingle.FORMAT_HOUR
                timerView?.onFinish = {
                    callback?.onOnGoingCampaignEnded(campaign)
                    listener?.showAlertCampaignEnded()
                }
            } else {
                timerView?.timerFormat = TimerUnifySingle.FORMAT_DAY
            }
            timerView?.show()
        } catch (ex: Exception) {
            this.hide()
        }
    }

    private fun getGradientDrawableForBackGround(gradientHexCodes: String, campaignTypes: Int = FLASH_SALE): GradientDrawable {
        return try {
            val gradientColors = gradientHexCodes.split(",")
            val firstColor = Color.parseColor(if (gradientColors[0].contains("#")) gradientColors[0] else "#${gradientColors[0]}")
            val secondColor = Color.parseColor(if (gradientColors[1].contains("#")) gradientColors[1] else "#${gradientColors[1]}")
            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(firstColor, secondColor))
        } catch (ex: Exception) {
            // return default gradient when the color parsing process is failed
            var firstColor = ContextCompat.getColor(context, R.color.product_detail_dms_default_green_bg_start_gradient_color)
            var secondColor = ContextCompat.getColor(context, R.color.product_detail_dms_default_green_bg_end_gradient_color)
            when (campaignTypes) {
                SLASH_PRICE -> {
                    firstColor = ContextCompat.getColor(context, R.color.product_detail_dms_default_red_bg_start_gradient_color)
                    secondColor = ContextCompat.getColor(context, R.color.product_detail_dms_default_red_bg_end_gradient_color)
                }
            }
            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(firstColor, secondColor))
        }
    }
}