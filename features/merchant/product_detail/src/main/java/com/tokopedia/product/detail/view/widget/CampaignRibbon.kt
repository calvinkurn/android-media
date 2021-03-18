package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
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
import kotlinx.android.synthetic.main.widget_campaign_ribbon_layout.view.*
import kotlinx.android.synthetic.main.widget_campaign_ribbon_type_1_layout.view.*
import kotlinx.android.synthetic.main.widget_campaign_ribbon_type_2_layout.view.*
import kotlinx.android.synthetic.main.widget_campaign_ribbon_type_3_layout.view.*
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
        private const val FLASH_SALE = 1
        private const val SLASH_PRICE = 2
        private const val NPL = 3
        private const val NEW_USER = 4
        const val THEMATIC_CAMPAIGN = 5

        // time unit
        private const val ONE_SECOND = 1000L
    }

    private var callback: CampaignCountDownCallback? = null
    private var listener: DynamicProductDetailListener? = null
    private var trackDataModel: ComponentTrackDataModel? = null

    init {
        addView(LayoutInflater.from(context).inflate(
                R.layout.widget_campaign_ribbon_layout,
                this,
                false))
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

    // FLASH SALE
    private fun renderFlashSaleCampaignRibbon(onGoingData: ProductContentMainData? = null) {
        onGoingData?.let {
            renderOnGoingCampaignRibbon(onGoingData)
            showCampaignRibbonType2()
        }
    }

    // SLASH PRICE - not eligible for thematic campaign
    private fun renderSlashPriceCampaignRibbon(onGoingData: ProductContentMainData) {
        val campaign = onGoingData.campaign
        if (campaign.shouldShowRibbonCampaign) {
            // thematic data
            val thematicCampaign = onGoingData.thematicCampaign
            // render campaign name
            val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
            tpg_campaign_name_s3.text = campaignName
            // render campaign ribbon background
            val gradientHexCodes = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else campaign.background
            val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes, SLASH_PRICE)
            campaign_ribbon_type_3?.background = gradientDrawable
            // show count down wording
            tpg_ends_in_s3.show()
            // render ongoing count down timer
            renderOnGoingCountDownTimer(campaign = campaign, timerView = tus_timer_view_s3)
            // hide irrelevant views
            tgp_regulatory_info_s3?.hide()
            iu_campaign_logo_s3?.hide()
            // show campaign ribbon type 3
            showCampaignRibbonType3()
        } else this.hide()
    }

    // THEMATIC ONLY
    private fun renderThematicCampaignRibbon(onGoingData: ProductContentMainData) {
        val thematicCampaign = onGoingData.thematicCampaign
        // render campaign ribbon background
        if (thematicCampaign.background.isNotBlank()) {
            campaign_ribbon_type_3?.background = getGradientDrawableForBackGround(thematicCampaign.background)
        }
        // render campaign logo
        if (thematicCampaign.icon.isNotBlank()) {
            iu_campaign_logo_s3.loadImage(thematicCampaign.icon)
            iu_campaign_logo_s3.show()
        } else iu_campaign_logo_s3.hide()
        // render campaign name
        tpg_campaign_name_s3.text = thematicCampaign.campaignName
        // hide irrelevant views
        tgp_regulatory_info_s3?.hide()
        tpg_ends_in_s3?.hide()
        tus_timer_view_s3?.hide()
        // show campaign ribbon type 3
        showCampaignRibbonType3()
    }

    //==========================================================================================================================================

    // show upcoming structure
    private fun showCampaignRibbonType1() {
        campaign_ribbon_type_1.show()
        campaign_ribbon_type_2.hide()
        campaign_ribbon_type_3.hide()
    }

    // show ongoing structure
    private fun showCampaignRibbonType2() {
        campaign_ribbon_type_1.hide()
        campaign_ribbon_type_2.show()
        campaign_ribbon_type_3.hide()
    }

    // show thematic only, new user, slash price structure
    private fun showCampaignRibbonType3() {
        campaign_ribbon_type_1.hide()
        campaign_ribbon_type_2.hide()
        campaign_ribbon_type_3.show()
    }

    fun renderUpComingCampaignRibbon(upcomingData: ProductNotifyMeDataModel?, upcomingIdentifier: String) {
        showCampaignRibbonType1()
        val gradientDrawable = if (upcomingIdentifier == ProductUpcomingTypeDef.UPCOMING_NPL) {
            context.getDrawable(R.drawable.bg_gradient_default_npl)
        } else {
            context.getDrawable(R.drawable.bg_gradient_default_flash_sale_by_seller)
        }

        // render campaign ribbon background
        gradientDrawable?.run { campaign_ribbon_layout_s1.background = gradientDrawable }
        // render campaign name
        val campaignTypeName = upcomingData?.upcomingNplData?.ribbonCopy ?: ""
        tpg_campaign_name_s1.text = if (campaignTypeName.isNotEmpty()) campaignTypeName else context.getString(R.string.notify_me_title)
        // count down timer
        upcomingData?.let {
            renderUpComingNplCountDownTimer(
                    it.startDate,
                    tus_timer_view_s1
            )
        }
        updateRemindMeButton(listener, upcomingData, upcomingIdentifier)
        // hide regulatory info
        regulatory_info_layout_s1.hide()
    }

    private fun renderUpComingNplCountDownTimer(startDateData: String,
                                                timerView: TimerUnifySingle) {
        try {
            val now = System.currentTimeMillis()
            val startTime = startDateData.toLongOrZero() * ONE_SECOND
            val startDate = Date(startTime)
            val calendar = Calendar.getInstance()
            calendar.time = startDate
            timerView.targetDate = calendar
            timerView.isShowClockIcon = false

            // less then 24 hours campaign period
            if (TimeUnit.MILLISECONDS.toDays(startDate.time - now) < 1) {
                timerView.timerFormat = TimerUnifySingle.FORMAT_HOUR
                timerView.onFinish = {
                    listener?.refreshPage()
                }
            } else {
                timerView.timerFormat = TimerUnifySingle.FORMAT_DAY
            }
            timerView.show()
        } catch (e: Throwable) {
            this.hide()
        }
    }

    private fun renderUpComingRemindMeButton(isOwner: Boolean,
                                             upComingData: ProductNotifyMeDataModel,
                                             remindMeButton: Typography, upcomingIdentifier: String) {
        remindMeButton.showWithCondition(!isOwner && upcomingIdentifier != ProductUpcomingTypeDef.UPCOMING_NPL)
        when (upComingData.notifyMe) {
            true -> {
                remindMeButton.text = context.getString(R.string.notify_me_active)
                remindMeButton.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
                remindMeButton.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_active)
            }
            false -> {
                remindMeButton.text = context.getString(R.string.notify_me_inactive)
                remindMeButton.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
                remindMeButton.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_inactive)
            }
        }
        remindMeButton.setOnClickListener {
            listener?.onNotifyMeClicked(upComingData, this.trackDataModel
                    ?: ComponentTrackDataModel())
        }
    }

    fun updateRemindMeButton(listener: DynamicProductDetailListener?, upComingData: ProductNotifyMeDataModel?, upcomingIdentifier: String) {
        upComingData?.let {
            renderUpComingRemindMeButton(listener?.isOwner()
                    ?: false, upComingData, remind_me_button_s1, upcomingIdentifier)
        }
    }

    private fun renderOnGoingCampaignRibbon(onGoingData: ProductContentMainData) {

        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign

        // render campaign ribbon background
        val gradientHexCodes = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else campaign.background
        val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
        campaign_ribbon_type_2?.background = gradientDrawable

        // render campaign logo
        if (thematicCampaign.icon.isNotBlank()) {
            iu_campaign_logo_s2.loadImage(thematicCampaign.icon)
            iu_campaign_logo_s2.scaleType = ImageView.ScaleType.FIT_CENTER
            iu_campaign_logo_s2.show()
        } else iu_campaign_logo_s2.hide()

        // render campaign name
        val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
        tpg_campaign_name_s2.text = campaignName

        // render ongoing count down
        renderOnGoingCountDownTimer(campaign = campaign, timerView = tus_timer_view_s2)

        // render stock wording
        val isStockWordingRendered = renderStockWording(
                stockSoldPercentage = campaign.stockSoldPercentage,
                stockWording = onGoingData.stockWording,
                stockTypography = tgp_stock_wording_s2,
                soldOutTypography = tgp_sold_out_wording_s2
        )

        // render stock bar
        renderStockBar(isStockWordingRendered, campaign.stockSoldPercentage, pbu_stock_bar_s2)

        // render regulatory info
        if (campaign.paymentInfoWording.isNotBlank()) {
            tgp_regulatory_info_s2.text = campaign.paymentInfoWording
            tgp_regulatory_info_s2?.show()
        } else {
            tgp_regulatory_info_s2?.hide()
        }
    }

    private fun renderStockWording(stockSoldPercentage: Int,
                                   stockWording: String,
                                   stockTypography: Typography,
                                   soldOutTypography: Typography): Boolean {
        return try {
            val styledStockWording = MethodChecker.fromHtml(stockWording)
            if (stockSoldPercentage == 1) {
                soldOutTypography.text = styledStockWording
                soldOutTypography.show()
            } else {
                stockTypography.text = styledStockWording
            }
            true
        } catch (ex: Exception) {
            stockTypography.hide()
            soldOutTypography.hide()
            false
        }
    }

    private fun renderStockBar(isStockWordingRendered: Boolean,
                               stockSoldPercentage: Int,
                               stockProgressBar: ProgressBarUnify) {
        if (!isStockWordingRendered) {
            stockProgressBar.hide()
            return
        }
        // set track color
        stockProgressBar.trackDrawable.setColor(ContextCompat.getColor(context, R.color.product_detail_dms_stock_bar_track_color))
        // set progressbar color gradient, if using 1 color then set the same color amount
        val stockBarColor = ContextCompat.getColor(context, R.color.product_detail_dms_stock_bar_progress_color)
        stockProgressBar.progressBarColor = intArrayOf(stockBarColor, stockBarColor)
        // percentage 100% = 1
        if (stockSoldPercentage != 1) {
            stockProgressBar.setValue(stockSoldPercentage)
            stockProgressBar.show()
        } else {
            stockProgressBar.hide()
        }
    }

    private fun renderOnGoingCountDownTimer(campaign: CampaignModular, timerView: TimerUnifySingle) {
        try {
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            val now = System.currentTimeMillis()
            val endDate = dateFormat.parse(campaign.endDate)
            val calendar = Calendar.getInstance()
            calendar.time = endDate
            timerView.targetDate = calendar
            timerView.isShowClockIcon = false

            // less then 24 hours campaign period
            if (TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1) {
                timerView.timerFormat = TimerUnifySingle.FORMAT_HOUR
                timerView.onFinish = {
                    callback?.onOnGoingCampaignEnded(campaign)
                    listener?.showAlertCampaignEnded()
                }
            } else {
                timerView.timerFormat = TimerUnifySingle.FORMAT_DAY
            }
            timerView.show()
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
            var firstColor = ContextCompat.getColor(context, R.color.product_detail_dms_to_green_gradient_color)
            var secondColor = ContextCompat.getColor(context, R.color.product_detail_dms_from_green_gradient_color)
            when (campaignTypes) {
                SLASH_PRICE -> {
                    firstColor = ContextCompat.getColor(context, R.color.product_detail_dms_to_red_gradient_color)
                    secondColor = ContextCompat.getColor(context, R.color.product_detail_dms_from_red_gradient_color)
                }
            }
            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(firstColor, secondColor))
        }
    }
}