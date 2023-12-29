package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.extensions.getDrawableChecker
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonLayoutBinding
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonType1LayoutBinding
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonType2LayoutBinding
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonType3LayoutBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class CampaignRibbon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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
        private const val ONE_THOUSAND = 1000L
    }

    private var _rootBinding: WidgetCampaignRibbonLayoutBinding? = null

    private val campaignType1Binding by lazy {
        _rootBinding?.campaignRibbonType1?.inflate()?.let {
            WidgetCampaignRibbonType1LayoutBinding.bind(it)
        }
    }

    private val campaignType2Binding by lazy {
        _rootBinding?.campaignRibbonType2?.inflate()?.let {
            WidgetCampaignRibbonType2LayoutBinding.bind(it)
        }
    }

    private val campaignType3Binding by lazy {
        _rootBinding?.campaignRibbonType3?.inflate()?.let {
            WidgetCampaignRibbonType3LayoutBinding.bind(it)
        }
    }

    // listeners , callback properties
    private var callback: CampaignCountDownCallback? = null
    private var listener: DynamicProductDetailListener? = null
    private var trackDataModel: ComponentTrackDataModel? = null

    init {
        val view = inflate(context, R.layout.widget_campaign_ribbon_layout, this)
        _rootBinding = WidgetCampaignRibbonLayoutBinding.bind(view)
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
                campaignType3Binding?.apply {
                    // render campaign name
                    tpgCampaignNameS3.text = campaign.campaignTypeName
                    // render campaign ribbon background
                    val backGroundColorData = campaign.background
                    renderBackGroundColor(root, backGroundColorData, SLASH_PRICE)
                    // show count down wording
                    tpgEndsInS3.show()
                    // render ongoing count down timer
                    renderOnGoingCountDownTimer(campaign = campaign, timerView = tusTimerViewS3)
                    // hide irrelevant views
                    tgpRegulatoryInfoS3.hide()
                    hideLogoView3()
                    // show campaign ribbon type 3
                    showCampaignRibbonType3()
                }
            } else this.hide()
        } else {
            // if thematic have value, render thematic instead of slash price
            renderThematicCampaignRibbon(onGoingData)
        }
    }

    // THEMATIC ONLY - use campaign ribbon structure type 3
    private fun renderThematicCampaignRibbon(onGoingData: ProductContentMainData) {
        campaignType3Binding?.apply {
            val thematicCampaign = onGoingData.thematicCampaign
            // render campaign ribbon background
            if (thematicCampaign.background.isNotBlank()) {
                val backGroundColorData = thematicCampaign.background

                renderBackGroundColor(root, backGroundColorData)
            }
            // render campaign logo
            if (thematicCampaign.icon.isNotBlank()) {
                iuCampaignLogoS3.loadImage(thematicCampaign.icon) {}
                showLogoView3()
            } else {
                hideLogoView3()
            }
            // render campaign name
            tpgCampaignNameS3.text = thematicCampaign.campaignName
            // hide irrelevant views
            tgpRegulatoryInfoS3.hide()
            tpgEndsInS3.hide()
            tusTimerViewS3.hide()
            // show campaign ribbon type 3
            showCampaignRibbonType3()
        }
    }

    private fun hideLogoView3() {
        campaignType3Binding?.apply {
            iuCampaignLogoS3.hide()
            iuCampaignLogoS3Container.hide()
        }
    }

    private fun showLogoView3() {
        campaignType3Binding?.apply {
            iuCampaignLogoS3.show()
            iuCampaignLogoS3Container.show()
        }
    }

    // show upcoming structure
    private fun showCampaignRibbonType1() {
        val root = _rootBinding ?: return

        root.showCampaignType1()
        root.showCampaignType2(show = false)
        root.showCampaignType3(show = false)
    }

    // show ongoing structure
    private fun showCampaignRibbonType2() {
        val root = _rootBinding ?: return

        root.showCampaignType1(show = false)
        root.showCampaignType2()
        root.showCampaignType3(show = false)
    }

    // show thematic only, new user, slash price structure
    private fun showCampaignRibbonType3() {
        val root = _rootBinding ?: return

        root.showCampaignType1(show = false)
        root.showCampaignType2(show = false)
        root.showCampaignType3()
    }

    // UPCOMING CAMPAIGN -  use campaign ribbon structure type 1
    fun renderUpComingCampaignRibbon(
        upcomingData: ProductNotifyMeDataModel?,
        upcomingIdentifier: String
    ) {
        showCampaignRibbonType1()
        renderUpcomingBackground(upcomingData, upcomingIdentifier)
        renderTimerUpcoming(upcomingData)
        val campaignTypeName = upcomingData?.upcomingNplData?.ribbonCopy ?: ""
        campaignType1Binding?.tpgCampaignNameS1?.text =
            campaignTypeName.ifEmpty { context.getString(R.string.notify_me_title) }
        updateRemindMeButton(listener, upcomingData, upcomingIdentifier)
    }

    private fun renderTimerUpcoming(upcomingData: ProductNotifyMeDataModel?) {
        val upComing = upcomingData ?: return
        val campaignType1 = campaignType1Binding ?: return

        renderUpComingNplCountDownTimer(
            upComing.startDate,
            campaignType1.tusTimerViewS1
        )
    }

    private fun renderUpcomingBackground(
        upcomingData: ProductNotifyMeDataModel?,
        upcomingIdentifier: String
    ) {
        val drawable = getUpcomingBackground(
            identifier = upcomingIdentifier,
            color = upcomingData?.bgColorUpcoming
        )

        campaignType1Binding?.root?.background = drawable
    }

    private fun getUpcomingBackground(
        identifier: String,
        color: String?
    ) = if (color.isNullOrBlank()) {
        if (identifier == ProductUpcomingTypeDef.UPCOMING_NPL) {
            context.getDrawableChecker(R.drawable.bg_gradient_default_blue)
        } else {
            context.getDrawableChecker(R.drawable.bg_gradient_default_red)
        }
    } else {
        getGradientDrawableForBackGround(color)
    }

    private fun renderUpComingNplCountDownTimer(
        startDateData: String,
        timerView: TimerUnifySingle?
    ) {
        try {
            val startTime = startDateData.toLongOrZero() * ONE_THOUSAND
            val startDate = Date(startTime)
            val calendar = Calendar.getInstance()
            calendar.time = startDate
            timerView?.targetDate = calendar
            timerView?.isShowClockIcon = false

            timerView?.timerFormat = TimerUnifySingle.FORMAT_AUTO
            timerView?.onFinish = {
                listener?.refreshPage()
            }
            timerView?.show()
        } catch (e: Throwable) {
            this.hide()
        }
    }

    private fun renderUpComingRemindMeButton(
        isOwner: Boolean,
        upComingData: ProductNotifyMeDataModel,
        remindMeButton: Typography?,
        upcomingIdentifier: String
    ) {
        remindMeButton?.showWithCondition(!isOwner && upcomingIdentifier != ProductUpcomingTypeDef.UPCOMING_NPL)
        when (upComingData.notifyMe) {
            true -> {
                remindMeButton?.text = context.getString(R.string.notify_me_active)
                remindMeButton?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                    )
                )
                remindMeButton?.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_active)
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

    fun updateRemindMeButton(
        listener: DynamicProductDetailListener?,
        upComingData: ProductNotifyMeDataModel?,
        upcomingIdentifier: String
    ) {
        val data = upComingData ?: return

        renderUpComingRemindMeButton(
            isOwner = listener?.isOwner().orFalse(),
            upComingData = data,
            remindMeButton = campaignType1Binding?.remindMeButtonS1,
            upcomingIdentifier = upcomingIdentifier
        )
    }

    // ONGOING CAMPAIGN - use campaign ribbon structure type 2
    private fun renderOnGoingCampaignRibbon(onGoingData: ProductContentMainData) {
        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign

        campaignType2Binding?.apply {
            // render campaign ribbon background
            val backGroundColorData = thematicCampaign.background.ifBlank { campaign.background }
            renderBackGroundColor(root, backGroundColorData)
            // render campaign logo
            if (thematicCampaign.icon.isNotBlank()) {
                iuCampaignLogoS2.loadImage(thematicCampaign.icon) {}
                showLogoView2()
            } else hideLogoView2()

            // render campaign name
            val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
            tpgCampaignNameS2.text = campaignName
            // render ongoing count down
            renderOnGoingCountDownTimer(campaign = campaign, timerView = tusTimerViewS2)
            // render stock wording
            renderStockWording(stockWording = onGoingData.stockWording, stockTypography = tgpStockWordingS2)
            // render stock bar
            renderStockBar(campaign.stockSoldPercentage, pbuStockBarS2)
            // render regulatory info
            tgpRegulatoryInfoS2.text = campaign.paymentInfoWording
            tgpRegulatoryInfoS2.isVisible = campaign.paymentInfoWording.isNotBlank()
        }
    }

    private fun hideLogoView2() {
        if (_rootBinding?.campaignRibbonType2?.isInflated() == true) {
            campaignType2Binding?.iuCampaignLogoS2?.hide()
            campaignType2Binding?.iuCampaignLogoS2Container?.hide()
        }
    }

    private fun showLogoView2() {
        if (_rootBinding?.campaignRibbonType2?.isInflated() == true) {
            campaignType2Binding?.iuCampaignLogoS2?.show()
            campaignType2Binding?.iuCampaignLogoS2Container?.show()
        }
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
            val endDateLong = campaign.endDateUnix.toLongOrNull()
            endDateLong?.run {
                val endDateMillis = this * ONE_THOUSAND
                val endDate = Date(endDateMillis)
                val calendar = Calendar.getInstance()
                calendar.time = endDate
                timerView?.targetDate = calendar
                timerView?.isShowClockIcon = false

                timerView?.timerFormat = TimerUnifySingle.FORMAT_AUTO
                timerView?.onFinish = {
                    callback?.onOnGoingCampaignEnded(campaign)
                    listener?.showAlertCampaignEnded()
                }
                timerView?.show()
            }
        } catch (ex: Exception) {
            this.hide()
        }
    }

    private fun renderBackGroundColor(view: View?, backGroundColorData: String, campaignTypes: Int = FLASH_SALE) {
        val colorCount = backGroundColorData.split(",").size
        if (colorCount == 1) {
            val backgroundColor = getBackGroundColor(backGroundColorData)
            view?.setBackgroundColor(backgroundColor)
        } else {
            val gradientDrawable = getGradientDrawableForBackGround(backGroundColorData, campaignTypes)
            view?.background = gradientDrawable
        }
    }

    private fun getBackGroundColor(colorString: String): Int {
        return try {
            Color.parseColor(colorString)
        } catch (ex: Exception) {
            ContextCompat.getColor(context, R.color.product_detail_dms_default_green_bg_start_gradient_color)
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


    private fun WidgetCampaignRibbonLayoutBinding.showCampaignType1(show: Boolean = true) {
        if (campaignRibbonType1.isInflated()) {
            campaignType1Binding?.root?.isVisible = show
        }
    }

    private fun WidgetCampaignRibbonLayoutBinding.showCampaignType2(show: Boolean = true) {
        if (campaignRibbonType2.isInflated()) {
            campaignType2Binding?.root?.isVisible = show
        }
    }

    private fun WidgetCampaignRibbonLayoutBinding.showCampaignType3(show: Boolean = true) {
        if (campaignRibbonType3.isInflated()) {
            campaignType3Binding?.root?.isVisible = show
        }
    }
}
