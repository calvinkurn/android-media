package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.model.datamodel.UpcomingNplDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.isGivenDateIsBelowThan24H
import com.tokopedia.unifycomponents.HtmlLinkHelper
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

        @IntDef(ONGOING, UPCOMING)
        @Retention(AnnotationRetention.SOURCE)
        annotation class CampaignPeriod

        const val ONGOING = 0
        const val UPCOMING = 1

        // campaign types
        private const val FLASH_SALE = 1
        private const val SLASH_PRICE = 2
        private const val NPL = 3
        private const val NEW_USER = 4
        private const val THEMATIC_CAMPAIGN = 5

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
        when (onGoingData.campaign.campaignIdentifier) {
            FLASH_SALE -> renderFlashSaleCampaignRibbon(campaignPeriod = ONGOING, onGoingData = onGoingData)
            SLASH_PRICE -> renderSlashPriceCampaignRibbon(onGoingData = onGoingData)
            NPL -> renderNplCampaignRibbon(campaignPeriod = ONGOING, onGoingData = onGoingData)
            NEW_USER -> renderNewUserCampaignRibbon(onGoingData = onGoingData)
            THEMATIC_CAMPAIGN -> renderThematicCampaignRibbon(onGoingData = onGoingData)
        }
    }

    fun renderUpComingCampaign(upComingData: ProductNotifyMeDataModel) {
        when (upComingData.campaignIdentifier) {
            FLASH_SALE -> renderFlashSaleCampaignRibbon(campaignPeriod = UPCOMING, upComingData = upComingData)
        }
    }

    // FLASH SALE
    private fun renderFlashSaleCampaignRibbon(@CampaignPeriod campaignPeriod: Int,
                                              onGoingData: ProductContentMainData? = null,
                                              upComingData: ProductNotifyMeDataModel? = null) {
        when (campaignPeriod) {
            UPCOMING -> {
                upComingData?.let {
                    renderUpComingCampaignRibbon(upComingData)
                    showCampaignRibbonType1()
                }
            }
            ONGOING -> {
                onGoingData?.let {
                    renderOnGoingCampaignRibbon(onGoingData)
                    showCampaignRibbonType2()
                }
            }
        }
    }

    // SLASH PRICE - not eligible for thematic campaign
    private fun renderSlashPriceCampaignRibbon(onGoingData: ProductContentMainData) {
        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign
        if (campaign.startDate.isGivenDateIsBelowThan24H()) {
            // render campaign name
            val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
            tpg_campaign_name_s3.text = campaignName
            // render campaign ribbon background
            val gradientHexCodes = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else campaign.background
            val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
            campaign_ribbon_layout_s3.background = gradientDrawable
            // render ongoing count down timer
            renderOnGoingCountDownTimer(campaign = campaign, timerView = tus_timer_view_s3)
            // hide irrelevant views
            regulatory_info_layout_s3.hide()
            // show campaign ribbon type 3
            showCampaignRibbonType3()
        } else {
            this.hide()
        }
    }

    // NPL
    fun renderNplCampaignRibbon(@CampaignPeriod campaignPeriod: Int,
                                onGoingData: ProductContentMainData? = null,
                                upcomingNplData: UpcomingNplDataModel? = null) {
        when (campaignPeriod) {
            UPCOMING -> {
                onGoingData?.let {
                    renderUpComingNplCampaignRibbon(onGoingData, upcomingNplData)
                    showCampaignRibbonType1()
                }
            }
            ONGOING -> {
                onGoingData?.let {
                    renderOnGoingCampaignRibbon(onGoingData)
                    showCampaignRibbonType2()
                }
            }
        }
    }

    // NEW USER
    private fun renderNewUserCampaignRibbon(onGoingData: ProductContentMainData) {
        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign
        if (campaign.startDate.isGivenDateIsBelowThan24H()) {
            renderOnGoingCampaignRibbon(onGoingData)
            showCampaignRibbonType1()
        } else {
            // render campaign name
            val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
            tpg_campaign_name_s3.text = campaignName
            // render campaign ribbon background
            val gradientHexCodes = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else campaign.background
            val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
            campaign_ribbon_layout_s3.background = gradientDrawable
            // hide irrelevant views
            regulatory_info_layout_s3.hide()
            tus_timer_view_s3.hide()
            // show campaign ribbon type 3
            showCampaignRibbonType3()
        }
    }

    // THEMATIC ONLY
    private fun renderThematicCampaignRibbon(onGoingData: ProductContentMainData) {
        val thematicCampaign = onGoingData.thematicCampaign
        // render campaign ribbon background
        if (thematicCampaign.background.isNotBlank()) {
            campaign_ribbon_layout_s3.background = getGradientDrawableForBackGround(thematicCampaign.background)
        }
        // render campaign logo
        if (thematicCampaign.icon.isNotBlank()) {
            iu_campaign_logo_s3.loadImage(thematicCampaign.icon)
            iu_campaign_logo_s3.show()
        } else iu_campaign_logo_s3.hide()
        // render campaign name
        tpg_campaign_name_s3.text = thematicCampaign.campaignName
        // show campaign ribbon type 3
        showCampaignRibbonType2()
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

    private fun renderUpComingNplCampaignRibbon(onGoingData: ProductContentMainData, upcomingNplData: UpcomingNplDataModel?) {
        val campaign = onGoingData.campaign
        // render campaign ribbon background
        val gradientDrawable = context.getDrawable(R.drawable.bg_gradient_default_npl)
        gradientDrawable?.run { campaign_ribbon_layout_s1.background = gradientDrawable }
        // render campaign name
        val campaignName = campaign.campaignTypeName
        tpg_campaign_name_s1.text = if (campaignName.isNotBlank()) campaignName else context.getString(R.string.notify_me_title)
        // count down timer
        upcomingNplData?.let {
            renderUpComingNplCountDownTimer(
                    it.startDate,
                    it.ribbonCopy,
                    tus_timer_view_s1,
                    tv_start_in_s1
            )
        }
        // hide remind me button
        remind_me_button_s1.hide()
        // hide regulatory info
        regulatory_info_layout_s1.hide()
    }

    private fun renderUpComingNplCountDownTimer(startDateData: String,
                                                ribbonCopy: String,
                                                timerView: TimerUnifySingle,
                                                timerWordingView: Typography) {
        if (startDateData.isGivenDateIsBelowThan24H()) {
            try {
                val now = System.currentTimeMillis()
                val startTime = startDateData.toLongOrZero() * ONE_SECOND
                val startDate = Date(startTime)

                if (TimeUnit.MILLISECONDS.toDays(startDate.time - now) < 1) {
                    timerView.show()
                    timerView.onFinish = {
                        listener?.refreshPage()
                    }
                    this.rootView.show()
                } else {
                    timerView.hide()
                    timerWordingView.gone()
                }
            } catch (e: Throwable) {
                this.rootView.hide()
            }
        } else {
            timerWordingView.text = MethodChecker.fromHtml(ribbonCopy)
            timerView.hide()
        }
    }

    private fun renderUpComingCampaignRibbon(upComingData: ProductNotifyMeDataModel) {
        // render campaign ribbon background
        val gradientDrawable = context.getDrawable(R.drawable.bg_gradient_default_flash_sale)
        campaign_ribbon_layout_s1.background = gradientDrawable
        // render campaign name
        val campaignName = upComingData.campaignTypeName
        tpg_campaign_name_s1.text = if (campaignName.isNotBlank()) campaignName else context.getString(R.string.notify_me_title)
        // render count down timer
        renderUpComingCountDownTimer(upComingData.startDate, tus_timer_view_s1)
        // remind me button
        renderUpComingRemindMeButton(listener?.isOwner()
                ?: false, upComingData, remind_me_button_s1)
        // hide regulatory info
        regulatory_info_layout_s1.hide()
    }

    private fun renderUpComingRemindMeButton(isOwner: Boolean,
                                             upComingData: ProductNotifyMeDataModel,
                                             remindMeButton: Typography) {
        remindMeButton.showWithCondition(isOwner)
        when (upComingData.notifyMe) {
            true -> {
                remindMeButton.text = context.getString(R.string.notify_me_active)
                remindMeButton.setTextColor(ContextCompat.getColor(context, R.color.Unify_N100))
                remindMeButton.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_active)
            }
            false -> {
                remindMeButton.text = context.getString(R.string.notify_me_inactive)
                remindMeButton.setTextColor(ContextCompat.getColor(context, R.color.Unify_Static_White))
                remindMeButton.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_inactive)
            }
        }
        remindMeButton.setOnClickListener {
            listener?.onNotifyMeClicked(upComingData, this.trackDataModel
                    ?: ComponentTrackDataModel())
        }
    }

    private fun renderUpComingCountDownTimer(startDateData: String, timerView: TimerUnifySingle) {
        try {
            val now = System.currentTimeMillis()
            val startTime = startDateData.toLongOrZero() * ONE_SECOND
            val dayLeft = TimeUnit.MILLISECONDS.toDays(startTime - now)

            this.rootView.show()
            when {
                dayLeft < 0 -> {
                    this.rootView.hide()
                }
                dayLeft < 1 -> {
                    timerView.onFinish = {
                        listener?.refreshPage()
                    }
                    timerView.show()
                    tv_start_in_s1.text = context.getString(R.string.notify_me_subtitle_main)
                }
                else -> {
                    timerView.gone()
                    tv_start_in_s1.text = HtmlLinkHelper(context,
                            context.getString(R.string.notify_me_subtitle, dayLeft.toInt().toString())
                    ).spannedString
                }
            }
        } catch (ex: Exception) {
            this.rootView.hide()
        }
    }

    fun updateRemindMeButton(listener: DynamicProductDetailListener, upComingData: ProductNotifyMeDataModel) {
        renderUpComingRemindMeButton(listener.isOwner(), upComingData, remind_me_button_s1)
    }

    private fun renderOnGoingCampaignRibbon(onGoingData: ProductContentMainData) {

        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign

        // render campaign ribbon background
        val gradientHexCodes = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else campaign.background
        val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
        campaign_ribbon_layout_s2.background = gradientDrawable

        // render campaign logo
        if (thematicCampaign.icon.isNotBlank()) {
            iu_campaign_logo_s2.loadImage(thematicCampaign.icon)
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
            regulatory_info_layout_s2.background = gradientDrawable
            regulatory_info_layout_s2.show()
        } else {
            regulatory_info_layout_s2.hide()
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

            if (TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1) {
                timerView.show()
                val calendar = Calendar.getInstance()
                calendar.time = endDate
                timerView.targetDate = calendar
                timerView.onFinish = {
                    callback?.onOnGoingCampaignEnded(campaign)
                    listener?.showAlertCampaignEnded()
                }
                this.show()
            } else {
                tgp_count_down_wording_s2.hide()
                timerView.hide()
            }
        } catch (ex: Exception) {
            this.hide()
        }
    }

    private fun getGradientDrawableForBackGround(gradientHexCodes: String): GradientDrawable {
        val gradientColors = gradientHexCodes.split(",")
        val firstColor = Color.parseColor(gradientColors[0])
        val secondColor = Color.parseColor(gradientColors[1])
        return GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(firstColor, secondColor))
    }
}