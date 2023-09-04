package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopPageConstant.ShopTickerType
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.convertUrlToBitmapAndLoadImage
import com.tokopedia.shop.databinding.ShopHeaderFragmentTabContentBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderPlayWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopPageHeaderRequestUnmoderateBottomSheet
import com.tokopedia.shop.pageheader.presentation.customview.ShopPageHeaderCenteredImageSpan
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopFollowButtonUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderTickerData
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_LOGO
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_RATING
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderPlayWidgetButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_ACTION
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_PERFORMANCE
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_PLAY
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import java.util.*

class ShopPageHeaderFragmentHeaderViewHolderV2(
    private val viewBinding: ShopHeaderFragmentTabContentBinding?,
    private val listenerHeader: ShopPageHeaderFragmentViewHolderListener?,
    private val shopPageTracking: ShopPageTrackingBuyer?,
    private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
    private val context: Context,
    private val shopPagePlayWidgetListener: ShopPageHeaderPlayWidgetViewHolder.Listener?,
    private val chooseAddressWidgetListener: ChooseAddressWidget.ChooseAddressWidgetListener?,
) {

    companion object{
        private const val CYCLE_DURATION = 5000L
    }

    private val chooseAddressWidget: ChooseAddressWidget?
        get() = viewBinding?.chooseAddressWidget
    private val backgroundImageShopHeader: ImageUnify?
        get() = viewBinding?.backgroundImageShopHeader
    private val backgroundVideoShopHeader: PlayerView?
        get() = viewBinding?.backgroundVideoShopHeader
    private val backgroundColorShopHeader: View?
        get() = viewBinding?.backgroundColorShopHeader
    private val textShopName: Typography?
        get() = viewBinding?.textShopName
    private val imageShopLogo: ImageUnify?
        get() = viewBinding?.imageShopLogo
    private val imageShopBadge: ImageUnify?
        get() = viewBinding?.imageShopBadge
    private val sectionShopBasicInfo: View?
        get() = viewBinding?.sectionShopBasicInfo
    private val sectionShopPerformance: View?
        get() = viewBinding?.sectionShopPerformance
    private val imageRatingIcon: ImageUnify?
        get() = viewBinding?.imageShopRatingIcon
    private val textRatingDescription: Typography?
        get() = viewBinding?.textRatingDescription
    private val performanceSectionDotSeparator: View?
        get() = viewBinding?.dotSeparatorShopPerformance
    private val textShopOnlineDescription: Typography?
        get() = viewBinding?.textOnlineDescription
    private val textDynamicUspPerformance: Typography?
        get() = viewBinding?.textDynamicUspPerformance
    private val imageOnlineIcon: ImageView?
        get() = viewBinding?.ivOnlineIcon
    private val buttonChat: UnifyButton?
        get() = viewBinding?.buttonChat
    private val buttonFollow: UnifyButton?
        get() = viewBinding?.buttonFollow
    private val widgetPlayRootContainer: View?
        get() = viewBinding?.widgetPlayRootContainer
    private val playSgcWidgetContainer: View?
        get() = viewBinding?.playSgcWidgetContainer
    private val tvStartCreateContentDesc: Typography?
        get() = viewBinding?.tvStartCreateContentDesc
    private val playSgcBtnStartLive: View?
        get() = viewBinding?.playSgcBtnStartLive
    private val tvStartCreateContent: Typography?
        get() = viewBinding?.tvStartCreateContent

    private var coachMark: CoachMark2? = null
    private val tickerShopStatus: Ticker? = viewBinding?.tickerShopStatus
    private var playVideoWrapper: PlayVideoWrapper? = null
    private var timer : Timer? = null
    private var currentIndexUspDynamicValue: Int = 0

    fun setupChooseAddressWidget(isMyShop: Boolean) {
        chooseAddressWidget?.apply {
            val isRollOutUser = true
            if (isRollOutUser && !isMyShop) {
                show()
                chooseAddressWidgetListener?.let {
                    bindChooseAddress(chooseAddressWidgetListener)
                }
            } else {
                hide()
            }
        }
    }

    fun setupShopHeader(
        listWidgetShopData: List<ShopPageHeaderWidgetUiModel>,
        shopFollowButtonUiModel: ShopFollowButtonUiModel,
        shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?,
        isOverrideTheme: Boolean
    ) {
        setHeaderBackground(shopHeaderConfig, isOverrideTheme)
        setShopLogoImage(listWidgetShopData)
        setShopBasicInfoSection(listWidgetShopData, shopHeaderConfig, isOverrideTheme)
        setShopPerformanceSection(listWidgetShopData, shopHeaderConfig, isOverrideTheme)
        setShopStatusSection(listWidgetShopData, shopHeaderConfig, isOverrideTheme)
        setShopActionSection(
            listWidgetShopData,
            shopFollowButtonUiModel,
            shopHeaderConfig,
            isOverrideTheme,
            shopHeaderConfig?.patternColorType.orEmpty()
        )
        setSgcPlaySection(listWidgetShopData)
    }

    fun setSgcPlaySection(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>) {
        val shopSgcPlayData = getShopSgcPlayData(listWidgetShopData)
        val modelComponent =
            shopSgcPlayData?.componentPages?.filterIsInstance<ShopPageHeaderPlayWidgetButtonComponentUiModel>()
                ?.firstOrNull()
        if (null != shopSgcPlayData && null != modelComponent?.shopPageHeaderDataModel) {
            widgetPlayRootContainer?.show()
            tvStartCreateContent?.setCompoundDrawablesWithIntrinsicBounds(
                MethodChecker.getDrawable(context, R.drawable.ic_content_creation),
                null,
                null,
                null
            )
            modelComponent.shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
                if (allowContentCreation(shopPageHeaderDataModel)) {
                    showPlayWidget()
                    setupTextContentSgcWidget(shopPageHeaderDataModel)
                    shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopPageHeaderDataModel.shopId)
                    playSgcBtnStartLive?.setOnClickListener {
                        shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = shopPageHeaderDataModel.shopId)
                        shopPagePlayWidgetListener?.onStartLiveStreamingClicked(
                            modelComponent,
                            shopSgcPlayData,
                            shopPageHeaderDataModel.broadcaster
                        )
                    }
                } else {
                    hidePlayWidget()
                }
            }
        } else {
            widgetPlayRootContainer?.hide()
        }
    }

    private fun showPlayWidget() {
        widgetPlayRootContainer?.show()
        playSgcWidgetContainer?.show()
    }

    private fun hidePlayWidget() {
        widgetPlayRootContainer?.hide()
        playSgcWidgetContainer?.hide()
    }

    private fun allowContentCreation(dataModel: ShopPageHeaderDataModel): Boolean {
        return (isStreamAllowed(dataModel) || isShortsVideoAllowed(dataModel)) && GlobalConfig.isSellerApp()
    }

    private fun setupTextContentSgcWidget(dataModel: ShopPageHeaderDataModel) {
        if (tvStartCreateContentDesc?.text?.isNotBlank() == true) return

        val betaTemplate = context.getString(R.string.shop_page_play_widget_beta_template)

        val imgBeta = MethodChecker.getDrawable(context, R.drawable.ic_play_beta_badge)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        val imgBetaSpan = imgBeta?.let { ShopPageHeaderCenteredImageSpan(it) }

        val span = SpannableString(
            MethodChecker.fromHtml(
                when {
                    isStreamAllowed(dataModel) && isShortsVideoAllowed(dataModel) -> {
                        context.getString(R.string.shop_page_play_widget_livestream_and_shorts_label)
                    }

                    isStreamAllowed(dataModel) -> {
                        context.getString(R.string.shop_page_play_widget_livestream_only_label)
                    }

                    isShortsVideoAllowed(dataModel) -> {
                        context.getString(R.string.shop_page_play_widget_shorts_only_label)
                    }

                    else -> {
                        ""
                    }
                }
            )
        )

        span.setSpan(
            imgBetaSpan,
            span.indexOf(betaTemplate),
            span.indexOf(betaTemplate) + betaTemplate.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        tvStartCreateContentDesc?.text = span
    }

    private fun isStreamAllowed(dataModel: ShopPageHeaderDataModel): Boolean {
        return dataModel.broadcaster.streamAllowed
    }

    private fun isShortsVideoAllowed(dataModel: ShopPageHeaderDataModel): Boolean {
        return dataModel.broadcaster.shortVideoAllowed
    }

    private fun getShopSgcPlayData(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>): ShopPageHeaderWidgetUiModel? {
        return listWidgetShopData.firstOrNull { it.name == SHOP_PLAY }
    }

    private fun setShopActionSection(
        listWidgetShopData: List<ShopPageHeaderWidgetUiModel>,
        shopFollowButtonUiModel: ShopFollowButtonUiModel,
        shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?,
        isOverrideTheme: Boolean,
        patternColorType: String
    ) {
        val shopActionButtonData = getShopActionButtonData(listWidgetShopData)
        if (null != shopActionButtonData) {
            buttonChat?.apply {
                show()
                setOnClickListener {
                    listenerHeader?.onChatButtonClicked()
                }
            }
            buttonFollow?.apply {
                show()
                updateFollowButton(shopFollowButtonUiModel)
            }
        } else {
            buttonChat?.hide()
            buttonFollow?.hide()
        }
    }

    private fun getShopActionButtonData(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>): ShopPageHeaderWidgetUiModel? {
        return listWidgetShopData.firstOrNull { it.name == SHOP_ACTION }
    }

    private fun setShopStatusSection(
        listWidgetShopData: List<ShopPageHeaderWidgetUiModel>,
        shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?,
        isOverrideTheme: Boolean
    ) {
        //TODO Replace color from BE with unify color -> need to implement this after BE has contract for the expected value
//        val lastOnlineColor = ShopUtil.getColorHexString(itemView.context, R.color.clr_dms_31353B)
//        val lastOnlineUnifyColor = ShopUtil.getColorHexString(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
//        val unifiedShopAdditionalInfo = shopAdditionalInfo.replace(lastOnlineColor, lastOnlineUnifyColor)
        val shopBasicData = getShopBasicInfoData(listWidgetShopData)
        val shopOnlineStatusIcon =
            getShopBasicDataShopNameComponent(shopBasicData)?.text?.getOrNull(1)?.icon.orEmpty()
        val shopStatusText =
            getShopBasicDataShopNameComponent(shopBasicData)?.text?.getOrNull(1)?.textHtml.orEmpty()
        textShopOnlineDescription?.text = MethodChecker.fromHtml(shopStatusText)
        imageOnlineIcon?.apply {
            if (shopOnlineStatusIcon.isNotEmpty()) {
                show()
                loadImage(shopOnlineStatusIcon)
            } else {
                hide()
            }
        }
        if(isOverrideTheme){
            val textColor = shopHeaderConfig?.colorSchema?.getColorSchema(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            )?.value.orEmpty()
            textShopName?.setTextColor(ShopUtil.parseColorFromHexString(textColor))
        }
    }

    private fun getShopPerformanceShopRatingComponent(shopPerformanceData: ShopPageHeaderWidgetUiModel?): ShopPageHeaderBadgeTextValueComponentUiModel? {
        return (shopPerformanceData?.componentPages?.firstOrNull { it.name == SHOP_RATING } as? ShopPageHeaderBadgeTextValueComponentUiModel)
    }

    private fun setShopPerformanceSection(
        listWidgetShopData: List<ShopPageHeaderWidgetUiModel>,
        shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?,
        isOverrideTheme: Boolean
    ) {
        val shopPerformanceData = getShopPerformanceData(listWidgetShopData)
        val appLink =
            getShopPerformanceShopRatingComponent(shopPerformanceData)?.text?.firstOrNull()?.textLink.orEmpty()
        val ratingText =
            getShopPerformanceShopRatingComponent(shopPerformanceData)?.text?.firstOrNull()?.textHtml.orEmpty()
        val initialUspValue = listWidgetShopData.getDynamicUspComponent()?.text?.map { it.textHtml }.orEmpty()
        val isShowRating = ratingText.isNotEmpty()
        val isShowDynamicUsp = initialUspValue.isNotEmpty()
        sectionShopPerformance?.shouldShowWithAction(isShowRating || isShowDynamicUsp) {}
        performanceSectionDotSeparator?.shouldShowWithAction(isShowRating && isShowDynamicUsp) {}
        imageRatingIcon?.shouldShowWithAction(isShowRating) {
            textRatingDescription?.apply {
                text = MethodChecker.fromHtml(ratingText)
                setOnClickListener {
                    listenerHeader?.onShopReviewClicked(appLink)
                }
            }
        }

        imageRatingIcon?.setOnClickListener {
            listenerHeader?.onShopReviewClicked(appLink)
        }
        configDynamicUsp(listWidgetShopData)
        if(isOverrideTheme) {
            val textColor = shopHeaderConfig?.colorSchema?.getColorSchema(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            )?.value.orEmpty()
            textRatingDescription?.setTextColor(ShopUtil.parseColorFromHexString(textColor))
            textDynamicUspPerformance?.setTextColor(ShopUtil.parseColorFromHexString(textColor))
        }
    }

    private fun configDynamicUsp(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>) {
        val listDynamicUspValue = listWidgetShopData.getDynamicUspComponent()?.text?.map { it.textHtml }.orEmpty()
        updateDynamicUspValue(listDynamicUspValue.firstOrNull().orEmpty())
        textDynamicUspPerformance?.setOnClickListener {
            listenerHeader?.onUspClicked(listDynamicUspValue)
        }
        startDynamicUspCycle(listWidgetShopData)
    }

    private fun setShopLogoImage(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>) {
        val shopBasicData = getShopBasicInfoData(listWidgetShopData)
        val shopLogoImageUrl = getShopBasicDataShopLogoComponent(shopBasicData)?.image.orEmpty()
        imageShopLogo?.loadImage(shopLogoImageUrl)
    }

    private fun getShopBasicInfoData(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>): ShopPageHeaderWidgetUiModel? {
        return listWidgetShopData.firstOrNull { it.name == SHOP_BASIC_INFO }
    }

    private fun getShopPerformanceData(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>): ShopPageHeaderWidgetUiModel? {
        return listWidgetShopData.firstOrNull { it.name == SHOP_PERFORMANCE }
    }

    private fun setShopBasicInfoSection(
        listWidgetShopData: List<ShopPageHeaderWidgetUiModel>,
        shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?,
        isOverrideTheme: Boolean
    ) {
        val shopBasicData = getShopBasicInfoData(listWidgetShopData)
        val shopBadgeImageUrl =
            getShopBasicDataShopNameComponent(shopBasicData)?.text?.firstOrNull()?.icon.orEmpty()
        val shopName =
            getShopBasicDataShopNameComponent(shopBasicData)?.text?.firstOrNull()?.textHtml.orEmpty()
        val appLink =
            getShopBasicDataShopNameComponent(shopBasicData)?.text?.firstOrNull()?.textLink.orEmpty()
        imageShopBadge?.loadImage(shopBadgeImageUrl)
        textShopName?.text = MethodChecker.fromHtml(shopName)
        sectionShopBasicInfo?.setOnClickListener {
            listenerHeader?.onClickShopBasicInfoSection(appLink)
        }
        if(isOverrideTheme){
            val textColor = shopHeaderConfig?.colorSchema?.getColorSchema(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            )?.value.orEmpty()
            textShopName?.setTextColor(ShopUtil.parseColorFromHexString(textColor))
        }
    }

    private fun getShopBasicDataShopLogoComponent(shopBasicData: ShopPageHeaderWidgetUiModel?): ShopPageHeaderImageOnlyComponentUiModel? {
        return (shopBasicData?.componentPages?.firstOrNull { it.name == SHOP_LOGO } as? ShopPageHeaderImageOnlyComponentUiModel)
    }

    private fun getShopBasicDataShopNameComponent(shopBasicData: ShopPageHeaderWidgetUiModel?): ShopPageHeaderBadgeTextValueComponentUiModel? {
        return (shopBasicData?.componentPages?.firstOrNull { it.name == SHOP_NAME } as? ShopPageHeaderBadgeTextValueComponentUiModel)
    }

    private fun setHeaderBackground(
        shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?,
        isOverrideTheme: Boolean
    ) {
        if(isOverrideTheme) {
            val backgroundImage = getBackgroundImage(shopHeaderConfig)
            val backgroundVideo = shopHeaderConfig?.getBackgroundObject(
                ShopPageHeaderLayoutUiModel.BgObjectType.VIDEO
            )
            val backgroundColor = shopHeaderConfig?.listBackgroundColor?.firstOrNull().orEmpty()
            if (null != backgroundVideo) {
                setHeaderBackgroundVideo(backgroundVideo.url)
            } else if (null != backgroundImage) {
                setHeaderBackgroundImage(backgroundImage.url)
            } else {
                setHeaderBackgroundColor(backgroundColor)
            }
        }
    }

    private fun getBackgroundImage(shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?): ShopPageHeaderLayoutUiModel.Config.BackgroundObject? {
        val imageJpg = shopHeaderConfig?.getBackgroundObject(
            ShopPageHeaderLayoutUiModel.BgObjectType.IMAGE_JPG
        )
        val imagePng = shopHeaderConfig?.getBackgroundObject(
            ShopPageHeaderLayoutUiModel.BgObjectType.IMAGE_PNG
        )
        return imageJpg.takeIf { it != null } ?: imagePng
    }

    private fun setHeaderBackgroundColor(backgroundColor: String) {
        backgroundVideoShopHeader?.hide()
        backgroundImageShopHeader?.hide()
        backgroundColorShopHeader?.apply {
            show()
            setBackgroundColor(ShopUtil.parseColorFromHexString(backgroundColor))
        }
    }

    private fun setHeaderBackgroundImage(imageUrl: String) {
        backgroundColorShopHeader?.hide()
        backgroundVideoShopHeader?.hide()
        backgroundImageShopHeader?.apply {
            backgroundColorShopHeader?.hide()
            show()
            loadImage(imageUrl)
        }
    }

    fun updateFollowButton(model: ShopFollowButtonUiModel) {
        val shopFollowButtonVariantType = ShopUtil.getShopFollowButtonAbTestVariant().orEmpty()
        val isFollowing = model.isFollowing
        buttonFollow?.apply {
            when (shopFollowButtonVariantType) {
                RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD -> {
                    // existing/old variant type follow button
                    buttonSize = UnifyButton.Size.MICRO
                    buttonVariant = UnifyButton.Variant.GHOST
                    buttonType =
                        UnifyButton.Type.ALTERNATE.takeIf { isFollowing } ?: UnifyButton.Type.MAIN
                }

                RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_SMALL -> {
                    // new variant type follow button micro size
                    buttonSize = UnifyButton.Size.MICRO
                    buttonVariant = UnifyButton.Variant.GHOST.takeIf { isFollowing }
                        ?: UnifyButton.Variant.FILLED
                    buttonType =
                        UnifyButton.Type.ALTERNATE.takeIf { isFollowing } ?: UnifyButton.Type.MAIN
                }

                RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_BIG -> {
                    // new variant type follow button small size
                    buttonSize = UnifyButton.Size.SMALL
                    buttonVariant = UnifyButton.Variant.GHOST.takeIf { isFollowing }
                        ?: UnifyButton.Variant.FILLED
                    buttonType =
                        UnifyButton.Type.ALTERNATE.takeIf { isFollowing } ?: UnifyButton.Type.MAIN
                }
            }
            val isShowLoading = model.isShowLoading
            isLoading = isShowLoading
            if (!isShowLoading)
                text = model.textLabel
            if (isFollowing) {
                removeCompoundDrawableFollowButton(model.textLabel)
                model.leftDrawableUrl = ""
            } else {
                setDrawableLeft(this, model.leftDrawableUrl, model.isNeverFollow, model.textLabel)
            }
            setOnClickListener {
                if (!isLoading)
                    listenerHeader?.onFollowButtonClicked()
            }
        }
    }

    private fun setDrawableLeft(
        button: UnifyButton,
        leftDrawableUrl: String,
        isUserNeverFollow: Boolean,
        textLabel: String
    ) {
        if (leftDrawableUrl.isNotBlank() && isUserNeverFollow) {
            convertUrlToBitmapAndLoadImage(
                context,
                leftDrawableUrl,
                16.toPx()
            ) {
                button.setDrawable(BitmapDrawable(context.resources, it))
            }
        } else {
            removeCompoundDrawableFollowButton(textLabel)
        }
    }

    private fun removeCompoundDrawableFollowButton(textLabel: String) {
        buttonFollow?.text = textLabel
    }

    fun updateShopTicker(headerTickerData: ShopPageHeaderTickerData, isMyShop: Boolean) {
        when {
            shouldShowShopStatusTicker(
                headerTickerData.shopInfo.statusInfo.statusTitle,
                headerTickerData.shopInfo.statusInfo.statusMessage
            ) -> {
                showShopStatusTicker(headerTickerData.shopInfo, isMyShop)
            }

            shouldShowShopStatusTicker(
                headerTickerData.shopOperationalHourStatus.tickerTitle,
                headerTickerData.shopOperationalHourStatus.tickerMessage
            ) -> {
                showShopOperationalHourStatusTicker(
                    headerTickerData.shopOperationalHourStatus,
                    isMyShop
                )
            }

            else -> {
                hideShopStatusTicker()
            }
        }
    }

    private fun shouldShowShopStatusTicker(title: String, message: String): Boolean {
        return !(title.isEmpty() && message.isEmpty())
    }

    private fun showShopOperationalHourStatusTicker(
        shopOperationalHourStatus: ShopOperationalHourStatus,
        isMyShop: Boolean = false,
    ) {
        tickerShopStatus?.show()
        tickerShopStatus?.tickerType = if (isMyShop) {
            Ticker.TYPE_WARNING
        } else {
            Ticker.TYPE_ANNOUNCEMENT
        }
        tickerShopStatus?.tickerTitle =
            HtmlLinkHelper(context, shopOperationalHourStatus.tickerTitle).spannedString.toString()
        tickerShopStatus?.setHtmlDescription(shopOperationalHourStatus.tickerMessage)
        tickerShopStatus?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listenerHeader?.onShopStatusTickerClickableDescriptionClicked(linkUrl)
            }

            override fun onDismiss() {}
        })
        if (isMyShop) {
            tickerShopStatus?.closeButtonVisibility = View.GONE
        } else {
            tickerShopStatus?.closeButtonVisibility = View.VISIBLE
        }
    }

    private fun showShopStatusTicker(shopInfo: ShopInfo, isMyShop: Boolean = false) {
        val statusTitle = shopInfo.statusInfo.statusTitle
        val shopStatus = shopInfo.statusInfo.shopStatus
        val shopTickerType = shopInfo.statusInfo.tickerType
        val statusMessage = shopInfo.statusInfo.statusMessage
        val shopId = shopInfo.shopCore.shopID
        val isOfficialStore = shopInfo.goldOS.isOfficialStore()
        val isGoldMerchant = shopInfo.goldOS.isGoldMerchant()
        tickerShopStatus?.show()
        tickerShopStatus?.tickerType = when (shopTickerType) {
            ShopTickerType.INFO -> Ticker.TYPE_ANNOUNCEMENT
            ShopTickerType.WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_WARNING
        }
        tickerShopStatus?.tickerTitle = MethodChecker.fromHtml(statusTitle).toString()
        tickerShopStatus?.setHtmlDescription(
            if (shopStatus == ShopStatusDef.MODERATED && isMyShop) {
                generateShopModerateTickerDescription(statusMessage)
            } else {
                statusMessage
            }
        )
        tickerShopStatus?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                // set tracker data based on shop status
                when (shopStatus) {
                    ShopStatusDef.CLOSED -> {
                        shopPageTracking?.sendOpenShop()
                        shopPageTracking?.clickOpenOperationalShop(
                            CustomDimensionShopPage
                                .create(
                                    shopId,
                                    isOfficialStore,
                                    isGoldMerchant
                                )
                        )
                    }

                    ShopStatusDef.NOT_ACTIVE -> {
                        shopPageTracking?.clickHowToActivateShop(
                            CustomDimensionShopPage
                                .create(
                                    shopId,
                                    isOfficialStore,
                                    isGoldMerchant
                                )
                        )
                    }
                }
                if (linkUrl == context.getString(R.string.shop_page_header_request_unmoderate_appended_text_dummy_url)) {
                    // linkUrl is from appended moderate description, show bottomsheet to request open moderate
                    listenerHeader?.setShopUnmoderateRequestBottomSheet(
                        ShopPageHeaderRequestUnmoderateBottomSheet.createInstance().apply {
                            init(listenerHeader)
                        }
                    )
                } else {
                    // original url, open web view
                    listenerHeader?.onShopStatusTickerClickableDescriptionClicked(linkUrl)
                }
            }

            override fun onDismiss() {}

        })

        // special handling for shop status incubated
        if (shopInfo.statusInfo.shopStatus == ShopStatusDef.INCUBATED) {
            // always show ticker close button if shop is incubated
            tickerShopStatus?.closeButtonVisibility = View.VISIBLE
        } else {
            // default general condition for shop ticker
            if (isMyShop) {
                tickerShopStatus?.closeButtonVisibility = View.GONE
            } else {
                tickerShopStatus?.closeButtonVisibility = View.VISIBLE
            }
        }
    }

    private fun hideShopStatusTicker() {
        tickerShopStatus?.hide()
    }

    private fun generateShopModerateTickerDescription(originalStatusMessage: String): String {
        // append action text to request open moderation
        val appendedText = context.getString(
            R.string.shop_page_header_request_unmoderate_appended_text,
            context.getString(R.string.shop_page_header_request_unmoderate_appended_text_dummy_url),
            context.getString(R.string.new_shop_page_header_shop_close_description_seller_clickable_text)
        )
        return originalStatusMessage + appendedText
    }

    fun showCoachMark(
        followStatusData: FollowStatus?,
        shopId: String,
        userId: String
    ) {
        val coachMarkList = arrayListOf<CoachMark2Item>().apply {
            getShopFollowButtonCoachMarkItem(followStatusData)?.let {
                add(it)
            }
            getChooseAddressWidgetCoachMarkItem()?.let {
                add(it)
            }
        }
        if (coachMarkList.isNotEmpty()) {
            coachMark = CoachMark2(context)
            coachMark?.isOutsideTouchable = true
            coachMark?.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    checkCoachMarkImpression(
                        onCoachMarkFollowButtonImpressed = {
                            listenerHeader?.saveFirstTimeVisit()
                            shopPageTracking?.impressionCoachMarkFollowUnfollowShop(shopId, userId)
                        },
                        onCoachMarkChooseAddressWidgetImpressed = {
                            ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(context)
                        }
                    )
                }
            })
            coachMark?.showCoachMark(coachMarkList)
            checkCoachMarkImpression(
                onCoachMarkFollowButtonImpressed = {
                    listenerHeader?.saveFirstTimeVisit()
                    shopPageTracking?.impressionCoachMarkFollowUnfollowShop(shopId, userId)
                },
                onCoachMarkChooseAddressWidgetImpressed = {
                    ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(context)
                }
            )
        }
    }

    private fun getChooseAddressWidgetCoachMarkItem(): CoachMark2Item? {
        val isNeedToShowCoachMark = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(context)
        return if (isNeedToShowCoachMark == true && chooseAddressWidget?.isShown == true) {
            chooseAddressWidget?.let {
                CoachMark2Item(
                    it,
                    context.getString(R.string.shop_page_choose_address_widget_coachmark_title),
                    context.getString(R.string.shop_page_choose_address_widget_coachmark_description)
                )
            }
        } else {
            null
        }
    }

    private fun checkCoachMarkImpression(
        onCoachMarkFollowButtonImpressed: () -> Unit,
        onCoachMarkChooseAddressWidgetImpressed: () -> Unit
    ) {
        coachMark?.coachMarkItem?.getOrNull(coachMark?.currentIndex.orZero())?.let {
            when (it.anchorView.id) {
                buttonFollow?.id.orZero() -> {
                    onCoachMarkFollowButtonImpressed.invoke()
                }

                chooseAddressWidget?.id.orZero() -> {
                    onCoachMarkChooseAddressWidgetImpressed.invoke()
                }

                else -> {}
            }
        }
    }

    private fun getShopFollowButtonCoachMarkItem(
        followStatusData: FollowStatus?
    ): CoachMark2Item? {
        val buttonFollowView = buttonFollow
        val coachMarkText = followStatusData?.followButton?.coachmarkText.orEmpty()
//        return if (coachMarkText.isNotBlank() && listenerHeader?.isFirstTimeVisit() == false && buttonFollowView != null) {
        return if (coachMarkText.isNotBlank() && buttonFollowView != null) {

            CoachMark2Item(
                anchorView = buttonFollowView,
                title = "",
                description = MethodChecker.fromHtml(coachMarkText)
            )
        } else {
            null
        }
    }

    fun updateShopName(shopName: String) {
        if (shopName.isNotEmpty()) {
            textShopName?.text = MethodChecker.fromHtml(shopName)
        }
    }

    private fun updateDynamicUspValue(valueUsp: String) {
        textDynamicUspPerformance?.shouldShowWithAction(valueUsp.isNotEmpty()) {
            textDynamicUspPerformance?.text = valueUsp
        }
    }

    fun cycleDynamicUspText(dynamicUspValue: String) {
        if (dynamicUspValue.isNotEmpty()) {
            animateDynamicUspText(Int.ZERO.toFloat())?.withEndAction {
                animateDynamicUspText(Int.ONE.toFloat())
                updateDynamicUspValue(dynamicUspValue)
            }
        }
    }

    private fun animateDynamicUspText(alphaValue: Float): ViewPropertyAnimator? {
        return textDynamicUspPerformance?.animate()?.alpha(alphaValue)?.setDuration(UnifyMotion.T2)
    }

    private fun setHeaderBackgroundVideo(videoUrl: String) {
        backgroundImageShopHeader?.hide()
        backgroundColorShopHeader?.hide()
        if (playVideoWrapper == null) {
            playVideoWrapper = PlayVideoWrapper.Builder(context).build()
            playVideoWrapper?.addListener(object: PlayVideoWrapper.Listener {
                override fun onVideoPlayerChanged(player: ExoPlayer) {
                    backgroundVideoShopHeader?.player = player
                }

                override fun onPlayerStateChanged(state: PlayVideoState) {}
            })
        }
        backgroundVideoShopHeader?.apply {
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            show()
            player = playVideoWrapper!!.videoPlayer
        }
        playVideoWrapper?.setRepeatMode(true)
        playVideoWrapper?.playUri(
            uri = Uri.parse(videoUrl),
        )
        playVideoWrapper?.mute(true)
    }

    fun pauseVideo() {
        playVideoWrapper?.pause(false)
    }

    fun resumeVideo() {
        playVideoWrapper?.resume()
    }

    fun releaseVideo() {
        playVideoWrapper?.release()
    }

    fun startDynamicUspCycle(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>) {
        val listDynamicUspValue = listWidgetShopData.getDynamicUspComponent()?.text?.map { it.textHtml }.orEmpty()
        if (timer == null && listDynamicUspValue.isNotEmpty()) {
            timer = Timer()
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (currentIndexUspDynamicValue == listDynamicUspValue.size - Int.ONE) {
                        currentIndexUspDynamicValue = Int.ZERO
                    } else {
                        ++currentIndexUspDynamicValue
                    }
                    val currentValue = listDynamicUspValue[currentIndexUspDynamicValue]
                    cycleDynamicUspText(currentValue)
                }
            }, CYCLE_DURATION, CYCLE_DURATION)
        }
    }

    fun clearTimerDynamicUsp() {
        timer?.cancel()
        timer = null
        currentIndexUspDynamicValue = Int.ZERO
        updateDynamicUspValue(String.EMPTY)
    }

    private fun List<ShopPageHeaderWidgetUiModel>?.getDynamicUspComponent(): ShopPageHeaderBadgeTextValueComponentUiModel? {
        return this?.firstOrNull {
            it.type == SHOP_BASIC_INFO
        }?.componentPages?.firstOrNull {
            it.name == BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_DYNAMIC_USP
        } as? ShopPageHeaderBadgeTextValueComponentUiModel
    }

    fun pauseTimerDynamicUspCycle() {
        timer?.cancel()
        timer = null
    }

    fun resumeTimerDynamicUspCycle() {

    }

}
