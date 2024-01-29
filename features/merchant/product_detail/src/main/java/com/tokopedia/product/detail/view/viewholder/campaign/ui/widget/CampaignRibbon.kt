package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.ThematicCampaign
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.extensions.getDrawableChecker
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonLayoutBinding
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonType1LayoutBinding
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonType2LayoutBinding
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonType3LayoutBinding
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.util.setContentUi
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.UpcomingCampaignUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.ongoing.OngoingCampaignComposeUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.upcoming.UpcomingCampaignComposeUiModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CampaignRibbon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {

        // campaign types
        const val NO_CAMPAIGN = 0
        const val FLASH_SALE = 1
        const val SLASH_PRICE = 2
        const val NPL = 3
        const val NEW_USER = 4
        const val THEMATIC_CAMPAIGN = 5

        // id 7 is campaign revamp for ongoing campaign included for ids 1,2,3,4
        // if there is a issue in the future, so BE will do fallback with return 1/2/3/4
        const val NEW_FLASH_SALE_CAMPAIGN = 7

        // time unit
        private const val ONE_THOUSAND = 1000L
    }

    /**
     * callback the component
     */
    private var onCampaignEnded: (campaign: CampaignModular) -> Unit = {}
    private var onRefreshPage: () -> Unit = {}
    private var onRemindMeClick: (UpcomingCampaignUiModel, ComponentTrackDataModel) -> Unit =
        { _, _ -> }

    /**
     * View Binding
     */
    private var _rootBinding: WidgetCampaignRibbonLayoutBinding? = null

    private val campaignType1Binding by lazyThreadSafetyNone {
        _rootBinding?.campaignRibbonType1?.inflate()?.let {
            WidgetCampaignRibbonType1LayoutBinding.bind(it)
        }
    }

    private val campaignType2Binding by lazyThreadSafetyNone {
        _rootBinding?.campaignRibbonType2?.inflate()?.let {
            WidgetCampaignRibbonType2LayoutBinding.bind(it)
        }
    }

    private val campaignType3Binding by lazyThreadSafetyNone {
        _rootBinding?.campaignRibbonType3?.inflate()?.let {
            WidgetCampaignRibbonType3LayoutBinding.bind(it)
        }
    }

    /**
     * UI State for Campign Compose
     */
    private val _campaignTypeState = MutableStateFlow<CampaignType>(CampaignType.None)
    private val campaignTypeState get() = _campaignTypeState.asStateFlow()

    /**
     * data tracker
     */
    private var trackDataModel: ComponentTrackDataModel? = null

    /**
     * Remote Config
     */
    private val remoteConfig by lazyThreadSafetyNone { FirebaseRemoteConfigImpl(context) }
    private val thematicComposeActive by lazyThreadSafetyNone {
        remoteConfig.getBoolean(RemoteConfigKey.ANDROID_PDP_THEMATIC_CAMPAIGN_COMPOSE_ENABLE, true)
    }
    private val upcomingComposeActive by lazyThreadSafetyNone {
        remoteConfig.getBoolean(RemoteConfigKey.ANDROID_PDP_UPCOMING_CAMPAIGN_COMPOSE_ENABLE, true)
    }

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.widget_campaign_ribbon_layout, this, true)
        _rootBinding = WidgetCampaignRibbonLayoutBinding.bind(view)
        setupCampaignCompose()
    }

    private fun setupCampaignCompose() {
        val binding = _rootBinding ?: return
        binding.campaignRibbonCompose.setContentUi {
            val type = campaignTypeState.collectAsStateWithLifecycle()
            binding.campaignRibbonCompose.isVisible = type.value != CampaignType.None

            NestTheme {
                type.value.Content()
            }
        }
    }

    fun init(
        onCampaignEnded: (campaign: CampaignModular) -> Unit = {},
        onRefreshPage: () -> Unit = {},
        onRemindMeClick: (UpcomingCampaignUiModel, ComponentTrackDataModel) -> Unit = { _, _ -> }
    ) {
        this.onCampaignEnded = onCampaignEnded
        this.onRefreshPage = onRefreshPage
        this.onRemindMeClick = onRemindMeClick
    }

    fun setComponentTrackDataModel(trackDataModel: ComponentTrackDataModel) {
        this.trackDataModel = trackDataModel
    }

    /**
     * Render priority => ongoing > mega thematic > upcoming > regular thematic
     */
    fun setData(onGoingData: ProductContentMainData?, upComingData: UpcomingCampaignUiModel?) {
        if (onGoingData != null) {
            renderOnGoingCampaignLegacy(onGoingData = onGoingData)
        } else if (upComingData != null) {
            renderUpComingCampaignRibbon(upComing = upComingData)
        }
    }

    /**
     * Ongoing Campaign Legacy
     */
    // region ongoing campaign legacy
    private fun renderOnGoingCampaignLegacy(onGoingData: ProductContentMainData) {
        val identifier = onGoingData.campaign.campaignIdentifier
        if (identifier == NO_CAMPAIGN) {
            hideComponent()
            return
        } else {
            showComponent()
        }

        when (identifier) {
            FLASH_SALE, NEW_USER, NPL -> renderFlashSaleCampaignRibbon(onGoingData = onGoingData)
            SLASH_PRICE -> renderSlashPriceCampaignRibbon(onGoingData = onGoingData)
            THEMATIC_CAMPAIGN -> renderThematicCampaign(data = onGoingData.thematicCampaign)
            NEW_FLASH_SALE_CAMPAIGN -> renderOngoingCampaign(
                data = onGoingData.campaign,
                stockWording = onGoingData.stockWording
            )
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
                    iuCampaignLogoS3.hide()
                    // show campaign ribbon type 3
                    showCampaignRibbonType3()
                }
            } else {
                hideComponent()
            }
        } else {
            // if thematic have value, render thematic instead of slash price
            renderThematicCampaignRibbon(onGoingData.thematicCampaign)
        }
    }

    // THEMATIC ONLY - use campaign ribbon structure type 3
    private fun renderThematicCampaignRibbon(thematic: ThematicCampaign) {
        campaignType3Binding?.apply {
            // render campaign ribbon background
            if (thematic.background.isNotBlank()) {
                val backGroundColorData = thematic.background

                renderBackGroundColor(root, backGroundColorData)
            }
            // render campaign logo
            iuCampaignLogoS3.showIfWithBlock(thematic.campaignLogo.isNotBlank()) {
                loadImageWithoutPlaceholder(thematic.campaignLogo)
            }
            tpgCampaignNameS3.showIfWithBlock(thematic.campaignLogo.isBlank()) {
                tpgCampaignNameS3.text = thematic.campaignName
                tpgCampaignNameS3.setTextColor(
                    if (thematic.superGraphicURL.isBlank()) {
                        context.getColorChecker(unifyprinciplesR.color.Unify_Static_Black)
                    } else {
                        context.getColorChecker(unifyprinciplesR.color.Unify_Static_White)
                    }
                )
            }

            tpgCampaignSupergraphicS3.showIfWithBlock(thematic.superGraphicURL.isNotBlank()) {
                loadImageWithoutPlaceholder(thematic.superGraphicURL)
            }

            // hide irrelevant views
            tgpRegulatoryInfoS3.hide()
            tpgEndsInS3.hide()
            tusTimerViewS3.hide()
            // show campaign ribbon type 3
            showCampaignRibbonType3()
        }
    }

    // show upcoming structure
    private fun showCampaignRibbonType1() {
        _rootBinding?.apply {
            showCampaignType1()
            showCampaignType2(show = false)
            showCampaignType3(show = false)
            campaignRibbonCompose.hide()
        }
    }

    // show ongoing structure
    private fun showCampaignRibbonType2() {
        _rootBinding?.apply {
            showCampaignType1(show = false)
            showCampaignType2()
            showCampaignType3(show = false)
            campaignRibbonCompose.hide()
        }
    }

    // show thematic only, new user, slash price structure
    private fun showCampaignRibbonType3() {
        _rootBinding?.apply {
            showCampaignType1(show = false)
            showCampaignType2(show = false)
            showCampaignType3()
            campaignRibbonCompose.hide()
        }
    }

    private fun switchUiComposeToLegacy() {
        _rootBinding?.apply {
            showCampaignType1(show = true)
            showCampaignType2(show = true)
            showCampaignType3(show = true)
            campaignRibbonCompose.hide()
        }
    }

    private fun switchUiLegacyToCompose() {
        _rootBinding?.apply {
            campaignRibbonCompose.show()
            showCampaignType1(show = false)
            showCampaignType2(show = false)
            showCampaignType3(show = false)
        }
    }
    // endregion ongoing campaign legacy

    /**
     * Upcoming Campaign Legacy
     */
    // region upcoming legacy
    private fun renderTimerUpcoming(upcomingData: UpcomingCampaignUiModel) {
        val campaignType1 = campaignType1Binding ?: return

        renderUpComingNplCountDownTimer(
            upcomingData.startDate,
            campaignType1.tusTimerViewS1
        )
    }

    private fun renderUpcomingBackground(upcomingData: UpcomingCampaignUiModel) {
        val drawable = getUpcomingBackground(upcomingData = upcomingData)

        campaignType1Binding?.root?.background = drawable
    }

    private fun getUpcomingBackground(upcomingData: UpcomingCampaignUiModel) = if (upcomingData.bgColorUpcoming.isBlank()) {
        if (upcomingData.isNpl) {
            context.getDrawableChecker(R.drawable.bg_gradient_default_blue)
        } else {
            context.getDrawableChecker(R.drawable.bg_gradient_default_red)
        }
    } else {
        getGradientDrawableForBackGround(upcomingData.bgColorUpcoming)
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
            timerView?.onFinish = onRefreshPage
            timerView?.show()
        } catch (e: Throwable) {
            hideComponent()
        }
    }
    // endregion upcoming legacy

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
            } else {
                hideLogoView2()
            }

            // render campaign name
            val campaignName = thematicCampaign.campaignName.ifBlank { campaign.campaignTypeName }
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
                timerView?.onFinish = { onCampaignEnded(campaign) }
                timerView?.show()
            }
        } catch (ex: Exception) {
            hideComponent()
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

    fun hideComponent() {
        setLayoutHeight(Int.ZERO)
        // set blank content to compose
        if (_campaignTypeState.value != CampaignType.None) {
            _campaignTypeState.value = CampaignType.None
        }
    }

    fun showComponent() {
        setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // render campaign revamp
    /**
     * Ongoing Campaign Compose
     */
    private fun renderOngoingCampaign(data: CampaignModular, stockWording: String) {
        switchUiLegacyToCompose()

        val onGoingCampaign = OngoingCampaignComposeUiModel(
            logoUrl = data.campaignLogo,
            title = data.campaignTypeName,
            endTimeUnix = data.endDateUnix.toLongOrZero(),
            timerLabel = context.getString(R.string.label_ends_in),
            stockPercentage = data.stockSoldPercentage,
            stockLabel = stockWording,
            backgroundColorString = data.background,
            paymentSpecific = data.paymentInfoWording
        )
        val onGoing = CampaignType.OnGoing(data = onGoingCampaign) {
            onCampaignEnded.invoke(data)
        }

        _campaignTypeState.value = onGoing
    }

    /**
     * Thematic Campaign Compose
     */
    private fun renderThematicCampaign(data: ThematicCampaign) {
        if (thematicComposeActive) {
            switchUiLegacyToCompose()
            renderThematicCampaignCompose(thematic = data)
        } else {
            switchUiComposeToLegacy()
            renderThematicCampaignRibbon(thematic = data)
        }
    }

    private fun renderThematicCampaignCompose(thematic: ThematicCampaign) {
        val type = CampaignType.Thematic(
            title = thematic.campaignName,
            logoUrl = thematic.campaignLogo,
            superGraphicUrl = thematic.superGraphicURL,
            backgroundColorString = thematic.background
        )

        _campaignTypeState.value = type
    }

    /**
     * Upcoming Campaign Compose
     */
    private fun renderUpComingCampaignRibbon(upComing: UpcomingCampaignUiModel) {
        if (upcomingComposeActive) {
            switchUiLegacyToCompose()
            renderUpcomingCampaignCompose(data = upComing)
        } else {
            switchUiComposeToLegacy()
            renderUpcomingCampaignView(upComing)
        }
    }

    private fun renderUpcomingCampaignView(upcomingData: UpcomingCampaignUiModel) {
        showCampaignRibbonType1()
        renderUpcomingBackground(upcomingData)
        renderTimerUpcoming(upcomingData)
        val campaignTypeName = upcomingData.ribbonCopy
        campaignType1Binding?.tpgCampaignNameS1?.text = campaignTypeName.ifEmpty { context.getString(R.string.notify_me_title) }
        updateRemindMeButtonView(upcomingData)
    }

    private fun renderUpcomingCampaignCompose(data: UpcomingCampaignUiModel) {
        val labelButton = context.getString(if (data.notifyMe) R.string.notify_me_active else R.string.notify_me_inactive)

        val type = CampaignType.UpComing(
            data = UpcomingCampaignComposeUiModel(
                logoUrl = data.campaignLogo,
                title = data.ribbonCopy.ifBlank {
                    context.getString(R.string.notify_me_title)
                },
                endTimeUnix = data.startDate.toLongOrZero(),
                timerLabel = context.getString(R.string.notify_me_subtitle_main),
                labelButton = labelButton,
                backgroundColorString = data.bgColorUpcoming,
                showRemainderButton = data.showReminderButton
            ),
            onTimerFinish = onRefreshPage,
            onClickRemindMe = {
                onRemindMeClick.invoke(data, trackDataModel ?: ComponentTrackDataModel())
            }
        )

        _campaignTypeState.value = type
    }

    /**
     * Update Upcoming Campaign Compose
     */
    fun updateRemindMeButton(upComingData: UpcomingCampaignUiModel) {
        if (upcomingComposeActive) {
            switchUiLegacyToCompose()
            renderUpcomingCampaignCompose(data = upComingData)
        } else {
            switchUiComposeToLegacy()
            updateRemindMeButtonView(upComingData)
        }
    }

    private fun updateRemindMeButtonView(upComingData: UpcomingCampaignUiModel) {
        val remindMeButton = campaignType1Binding?.remindMeButtonS1
        remindMeButton?.showWithCondition(upComingData.showReminderButton)

        when (upComingData.notifyMe) {
            true -> {
                remindMeButton?.text = context.getString(R.string.notify_me_active)
                remindMeButton?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_Static_White
                    )
                )
                remindMeButton?.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_active)
            }
            false -> {
                remindMeButton?.text = context.getString(R.string.notify_me_inactive)
                remindMeButton?.setTextColor(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White))
                remindMeButton?.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_inactive)
            }
        }
        remindMeButton?.setOnClickListener {
            onRemindMeClick(upComingData, trackDataModel ?: ComponentTrackDataModel())
        }
    }
    // endregion upcoming campaign
}
