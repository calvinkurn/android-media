package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.TextView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.creation.common.presentation.customviews.ContentCreationEntryPointWidget
import com.tokopedia.creation.common.presentation.model.ContentCreationEntryPointSource
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopPageConstant.ShopTickerType
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.util.ShopTimer
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.convertUrlToBitmapAndLoadImage
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ShopHeaderFragmentTabContentBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderPlayWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopPageHeaderRequestUnmoderateBottomSheet
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopFollowButtonUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderTickerData
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.FREE_SHIPPING
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
import com.tokopedia.stories.widget.StoriesWidgetLayout
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopPageHeaderFragmentHeaderViewHolderV2(
    private val viewBinding: ShopHeaderFragmentTabContentBinding?,
    private val listenerHeader: ShopPageHeaderFragmentViewHolderListener?,
    private val shopPageTracking: ShopPageTrackingBuyer?,
    private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
    private val context: Context,
    private val shopPagePlayWidgetListener: ShopPageHeaderPlayWidgetViewHolder.Listener?,
    private val chooseAddressWidgetListener: ChooseAddressWidget.ChooseAddressWidgetListener?
) {

    companion object {
        private const val CYCLE_DURATION = 3500L
        private const val MAXIMUM_WIDTH_STATIC_USP = 100
        private const val NEW_SELLER_TEXT_HTML = "Penjual Baru"
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
    private val chevronIcon: IconUnify?
        get() = viewBinding?.shopPageChevronShopInfo
    private val imageShopLogo: ImageUnify?
        get() = viewBinding?.imageShopLogo
    private val imageShopBadge: ImageUnify?
        get() = viewBinding?.imageShopBadge
    private val sectionShopBasicInfo: View?
        get() = viewBinding?.sectionShopBasicInfo
    private val sectionShopPerformance: View?
        get() = viewBinding?.sectionShopPerformance
    private val imageRatingIcon: IconUnify?
        get() = viewBinding?.imageShopRatingIcon
    private val textRatingDescription: Typography?
        get() = viewBinding?.textRatingDescription
    private val performanceSectionDotSeparator: Typography?
        get() = viewBinding?.dotSeparatorShopPerformance
    private val textDynamicUspPerformance: Typography?
        get() = viewBinding?.textDynamicUspPerformance
    private val textShopOnlineDescription: Typography?
        get() = viewBinding?.textOnlineDescription
    private val sectionShopStatus: View?
        get() = viewBinding?.sectionShopStatus
    private val imageOnlineIcon: ImageView?
        get() = viewBinding?.ivOnlineIcon
    private val shopStatusSectionDotSeparator: Typography?
        get() = viewBinding?.dotSeparatorShopStatus
    private val imageShopStaticUsp: ImageUnify?
        get() = viewBinding?.imageShopStaticUsp
    private val buttonChat: UnifyButton?
        get() = viewBinding?.buttonChat
    private val buttonFollow: UnifyButton?
        get() = viewBinding?.buttonFollow
    private val widgetPlayRootContainer: ContentCreationEntryPointWidget?
        get() = viewBinding?.widgetPlayEntryPoint

    private val shopLogoContainer: StoriesWidgetLayout?
        get() = viewBinding?.imageShopContainer

    private var coachMark: CoachMark2? = null
    private val tickerShopStatus: Ticker? = viewBinding?.tickerShopStatus
    private var playVideoWrapper: PlayVideoWrapper? = null
    private var currentIndexUspDynamicValue: Int = 0
    private val timer = ShopTimer()

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
        isOverrideTheme: Boolean,
        shopPageHeaderDataModel: ShopPageHeaderDataModel?
    ) {
        setHeaderBackground(shopHeaderConfig, isOverrideTheme)
        setShopLogoImage(listWidgetShopData, shopPageHeaderDataModel)
        setShopBasicInfoSection(listWidgetShopData, shopHeaderConfig, isOverrideTheme, shopPageHeaderDataModel)
        setShopPerformanceSection(listWidgetShopData, shopHeaderConfig, isOverrideTheme)
        setShopStatusSection(listWidgetShopData, shopHeaderConfig, isOverrideTheme)
        setShopActionSection(
            listWidgetShopData,
            shopFollowButtonUiModel,
            shopHeaderConfig,
            isOverrideTheme,
            shopHeaderConfig?.getFinalPatternColorType(context.isDarkMode()).orEmpty()
        )
        setSgcPlaySection(listWidgetShopData, shopHeaderConfig)
    }

    fun setSgcPlaySection(
        listWidgetShopData: List<ShopPageHeaderWidgetUiModel>,
        shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?,
    ) {
        val shopSgcPlayData = getShopSgcPlayData(listWidgetShopData)
        val modelComponent =
            shopSgcPlayData?.componentPages?.filterIsInstance<ShopPageHeaderPlayWidgetButtonComponentUiModel>()
                ?.firstOrNull()

        if (null != shopSgcPlayData && modelComponent != null) {
            listenerHeader?.let {
                widgetPlayRootContainer?.creationBottomSheetListener =
                    it.getContentCreationListener()
            }
            widgetPlayRootContainer?.widgetSource = ContentCreationEntryPointSource.Shop
            widgetPlayRootContainer?.onClickListener = {
                shopPageTrackingSGCPlayWidget?.onClickSGCContent(
                    shopId = modelComponent.shopPageHeaderDataModel?.shopId.orEmpty()
                )
                shopPagePlayWidgetListener?.onStartLiveStreamingClicked(
                    modelComponent,
                    shopSgcPlayData,
                )
            }
            shopHeaderConfig?.let {
                widgetPlayRootContainer?.iconColor?.value = it.colorSchema.getColorIntValue(
                    ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR
                ).orZero()
                widgetPlayRootContainer?.textColor?.value = it.colorSchema.getColorIntValue(
                    ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
                ).orZero()
            }
            widgetPlayRootContainer?.fetchConfig()

            shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = modelComponent.shopPageHeaderDataModel?.shopId.orEmpty())
        }
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
        if (isOverrideTheme) {
            buttonChat?.applyColorMode(ColorMode.LIGHT_MODE)
            buttonFollow?.applyColorMode(ColorMode.LIGHT_MODE)
        } else {
            buttonChat?.applyColorMode(ColorMode.DEFAULT)
            buttonFollow?.applyColorMode(ColorMode.DEFAULT)
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
        val shopBasicData = getShopBasicInfoData(listWidgetShopData)
        val shopPerformanceData = getShopPerformanceData(listWidgetShopData)
        val shopOnlineStatusIcon = getShopBasicDataShopNameComponent(
            shopBasicData
        )?.text?.getOrNull(Int.ONE)?.icon.orEmpty()
        val shopStatusText = getShopBasicDataShopNameComponent(
            shopBasicData
        )?.text?.getOrNull(Int.ONE)?.textHtml.orEmpty()
        val shopStaticUspUrl =
            getShopPerformanceFreeShippingComponent(shopPerformanceData)?.image.orEmpty()
        val isShowShopOnlineStatus = shopStatusText.isNotEmpty()
        val isShowShopStaticUsp = shopStaticUspUrl.isNotEmpty()
        sectionShopStatus?.shouldShowWithAction(isShowShopOnlineStatus || isShowShopStaticUsp) {}
        shopStatusSectionDotSeparator?.shouldShowWithAction(isShowShopOnlineStatus && isShowShopStaticUsp) {}
        imageShopStaticUsp?.shouldShowWithAction(isShowShopStaticUsp) {
            imageShopStaticUsp?.loadImage(shopStaticUspUrl) {
                overrideSize(
                    Resize(
                        MAXIMUM_WIDTH_STATIC_USP.toPx(),
                        imageShopStaticUsp?.layoutParams?.height.orZero()
                    )
                )
            }
        }
        val onlineStatusTextPairHtmlColor: Pair<String, String>
        if (isOverrideTheme) {
            val textColor = shopHeaderConfig?.colorSchema?.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            ).orZero()
            val textColorHighEmphasisHex = shopHeaderConfig?.colorSchema?.getColorSchema(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            )?.value.orEmpty()
            val textColorLowEmphasisHex = shopHeaderConfig?.colorSchema?.getColorSchema(
                ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
            )?.value.orEmpty()
            shopStatusSectionDotSeparator?.setTextColor(textColor)
            onlineStatusTextPairHtmlColor = Pair(textColorHighEmphasisHex, textColorLowEmphasisHex)
        } else {
            val textColorHighEmphasisHex = ShopUtil.getColorHexString(
                context,
                unifyprinciplesR.color.Unify_NN950
            )
            val textColorLowEmphasisHex = ShopUtil.getColorHexString(
                context,
                unifyprinciplesR.color.Unify_NN600
            )
            onlineStatusTextPairHtmlColor = Pair(textColorHighEmphasisHex, textColorLowEmphasisHex)
        }
        textShopOnlineDescription?.shouldShowWithAction(isShowShopOnlineStatus) {
            textShopOnlineDescription?.text = MethodChecker.fromHtml(shopStatusText)
            textShopOnlineDescription?.apply {
                val adjustedShopStatusText = shopStatusText.replace(
                    "\$highEmphasis",
                    onlineStatusTextPairHtmlColor.first
                ).replace(
                    "\$lowEmphasis",
                    onlineStatusTextPairHtmlColor.second
                )
                text = MethodChecker.fromHtml(adjustedShopStatusText)
            }
            imageOnlineIcon?.apply {
                if (shopOnlineStatusIcon.isNotEmpty()) {
                    show()
                    loadImage(shopOnlineStatusIcon)
                } else {
                    hide()
                }
            }
        }
    }

    private fun getShopPerformanceShopRatingComponent(shopPerformanceData: ShopPageHeaderWidgetUiModel?): ShopPageHeaderBadgeTextValueComponentUiModel? {
        return (shopPerformanceData?.componentPages?.firstOrNull { it.name == SHOP_RATING } as? ShopPageHeaderBadgeTextValueComponentUiModel)
    }

    private fun getShopPerformanceFreeShippingComponent(shopPerformanceData: ShopPageHeaderWidgetUiModel?): ShopPageHeaderImageOnlyComponentUiModel? {
        return (shopPerformanceData?.componentPages?.firstOrNull { it.name == FREE_SHIPPING } as? ShopPageHeaderImageOnlyComponentUiModel)
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
        val initialUspValue =
            listWidgetShopData.getDynamicUspComponent()?.text?.map { it.textHtml }.orEmpty()
        val isShowRating = ratingText.isNotEmpty()
        val isShowDynamicUsp = initialUspValue.isNotEmpty()
        val ratingTextPairHtmlColor: Pair<String, String>
        sectionShopPerformance?.shouldShowWithAction(isShowRating || isShowDynamicUsp) {}
        performanceSectionDotSeparator?.shouldShowWithAction(isShowRating && isShowDynamicUsp) {}
        configDynamicUsp(listWidgetShopData)
        if (isOverrideTheme) {
            val textColorHighEmphasis = shopHeaderConfig?.colorSchema?.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            ).orZero()
            val textColorHighEmphasisHex = shopHeaderConfig?.colorSchema?.getColorSchema(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            )?.value.orEmpty()
            val textColorLowEmphasisHex = shopHeaderConfig?.colorSchema?.getColorSchema(
                ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
            )?.value.orEmpty()
            textDynamicUspPerformance?.setTextColor(textColorHighEmphasis)
            performanceSectionDotSeparator?.setTextColor(textColorHighEmphasis)
            ratingTextPairHtmlColor = Pair(textColorHighEmphasisHex, textColorLowEmphasisHex)
        } else {
            val textColorHighEmphasisHex = ShopUtil.getColorHexString(
                context,
                unifyprinciplesR.color.Unify_NN950
            )
            val textColorLowEmphasisHex = ShopUtil.getColorHexString(
                context,
                unifyprinciplesR.color.Unify_NN600
            )
            ratingTextPairHtmlColor = Pair(textColorHighEmphasisHex, textColorLowEmphasisHex)
        }
        imageRatingIcon?.shouldShowWithAction(isShowRating) {
            val adjustedRatingText = ratingText.replace(
                "\$highEmphasis",
                ratingTextPairHtmlColor.first
            ).replace(
                "\$lowEmphasis",
                ratingTextPairHtmlColor.second
            )

            textRatingDescription?.apply {
                text = MethodChecker.fromHtml(adjustedRatingText)
                setOnClickListener { handleShopReviewClick(adjustedRatingText, appLink) }
            }
            imageRatingIcon?.setOnClickListener {
                handleShopReviewClick(adjustedRatingText, appLink)
            }
        }
    }

    private fun handleShopReviewClick(adjustedRatingText: String, appLink: String) {
        val isNewSeller = adjustedRatingText.contains(NEW_SELLER_TEXT_HTML, ignoreCase = true)
        if (isNewSeller) return

        listenerHeader?.onShopReviewClicked(appLink)
    }

    private fun configDynamicUsp(listWidgetShopData: List<ShopPageHeaderWidgetUiModel>) {
        val listDynamicUspValue =
            listWidgetShopData.getDynamicUspComponent()?.text?.map { it.textHtml }.orEmpty()
        updateDynamicUspValue(listDynamicUspValue.firstOrNull().orEmpty())
        textDynamicUspPerformance?.setOnClickListener {
            listenerHeader?.onUspClicked(listDynamicUspValue)
        }
        startDynamicUspCycle(listWidgetShopData)
    }

    private fun setShopLogoImage(
        listWidgetShopData: List<ShopPageHeaderWidgetUiModel>,
        shopPageHeaderDataModel: ShopPageHeaderDataModel?
    ) {
        val shopBasicData = getShopBasicInfoData(listWidgetShopData)
        val shopBasicDataLogoComponent = getShopBasicDataShopLogoComponent(shopBasicData)
        val shopAvatarDataFromListWidget = shopBasicDataLogoComponent?.image.orEmpty()
        val shopAvatarDataFromShopHeader = shopPageHeaderDataModel?.avatar.orEmpty()
        val shopLogoImageUrl = shopAvatarDataFromListWidget.takeIf {
            it.isNotEmpty()
        } ?: shopAvatarDataFromShopHeader
        imageShopLogo?.loadImageCircle(shopLogoImageUrl)
        val shopId = shopBasicDataLogoComponent?.shopId.orEmpty()
        shopLogoContainer?.run {
            listenerHeader?.getStoriesWidgetManager()?.manage(this, shopId)
        }
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
        isOverrideTheme: Boolean,
        shopPageHeaderDataModel: ShopPageHeaderDataModel?
    ) {
        val shopBasicData = getShopBasicInfoData(listWidgetShopData)
        val shopBadgeImageUrlFromListWidget = getShopBasicDataShopNameComponent(shopBasicData)?.text?.firstOrNull()?.icon.orEmpty()
        val shopBadgeImageUrlFromShopHeader = shopPageHeaderDataModel?.shopBadge.orEmpty()
        val shopBadgeImageUrl = shopBadgeImageUrlFromListWidget.takeIf {
            it.isNotEmpty()
        } ?: shopBadgeImageUrlFromShopHeader
        val shopNameFromListWidget = getShopBasicDataShopNameComponent(shopBasicData)?.text?.firstOrNull()?.textHtml.orEmpty()
        val shopNameFromShopHeader = shopPageHeaderDataModel?.shopName.orEmpty()
        val shopName = shopNameFromListWidget.takeIf { it.isNotEmpty() } ?: shopNameFromShopHeader
        val appLink =
            getShopBasicDataShopNameComponent(shopBasicData)?.text?.firstOrNull()?.textLink.orEmpty()
        imageShopBadge?.shouldShowWithAction(shopBadgeImageUrl.isNotEmpty()) {
            imageShopBadge?.loadImage(shopBadgeImageUrl)
        }
        textShopName?.text = MethodChecker.fromHtml(shopName)
        sectionShopBasicInfo?.setOnClickListener {
            listenerHeader?.onClickShopBasicInfoSection(appLink)
        }
        if (isOverrideTheme) {
            val highEmphasisColor = shopHeaderConfig?.colorSchema?.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            ).orZero()
            textShopName?.setTextColor(highEmphasisColor)
            chevronIcon?.setColorFilter(highEmphasisColor, PorterDuff.Mode.SRC_ATOP)
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
        if (isOverrideTheme) {
            val backgroundImage = getBackgroundImage(shopHeaderConfig)
            val backgroundVideo = shopHeaderConfig?.getBackgroundObject(
                ShopPageHeaderLayoutUiModel.BgObjectType.VIDEO
            )
            val backgroundColor = shopHeaderConfig?.listBackgroundColor?.firstOrNull().orEmpty()
            setHeaderBackgroundColor(backgroundColor)
            if (null != backgroundVideo) {
                setHeaderBackgroundVideo(backgroundVideo.url)
            } else if (null != backgroundImage) {
                setHeaderBackgroundImage(backgroundImage.url)
            }
        }
    }

    private fun getBackgroundImage(shopHeaderConfig: ShopPageHeaderLayoutUiModel.Config?): ShopPageHeaderLayoutUiModel.Config.BackgroundObject? {
        return shopHeaderConfig?.getBackgroundObject(ShopPageHeaderLayoutUiModel.BgObjectType.IMAGE)
    }

    private fun setHeaderBackgroundColor(backgroundColor: String) {
        backgroundColorShopHeader?.apply {
            show()
            val finalBackgroundColor = if (backgroundColor.isEmpty()) {
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN0
                )
            } else {
                ShopUtil.parseColorFromHexString(backgroundColor)
            }
            setBackgroundColor(finalBackgroundColor)
        }
    }

    private fun setHeaderBackgroundImage(imageUrl: String) {
        backgroundVideoShopHeader?.hide()
        backgroundImageShopHeader?.apply {
            show()
            loadImage(imageUrl)
        }
    }

    fun updateFollowButton(model: ShopFollowButtonUiModel) {
        val isFollowing = model.isFollowing
        buttonFollow?.apply {
            buttonSize = UnifyButton.Size.MICRO
            buttonVariant =
                UnifyButton.Variant.FILLED.takeIf { !isFollowing } ?: UnifyButton.Variant.GHOST
            buttonType = UnifyButton.Type.MAIN
            val isShowLoading = model.isShowLoading
            if (!isShowLoading) {
                if (isFollowing) {
                    removeCompoundDrawableFollowButton()
                    model.leftDrawableUrl = ""
                } else {
                    setDrawableLeft(this, model.leftDrawableUrl, model.isNeverFollow)
                }
                text = model.textLabel
            } else {
                removeCompoundDrawableFollowButton()
                text = ""
            }
            isLoading = isShowLoading
            setOnClickListener {
                if (!isLoading) {
                    listenerHeader?.onFollowButtonClicked()
                }
            }
        }
    }

    private fun setDrawableLeft(
        button: UnifyButton,
        leftDrawableUrl: String,
        isUserNeverFollow: Boolean
    ) {
        if (leftDrawableUrl.isNotBlank() && isUserNeverFollow) {
            convertUrlToBitmapAndLoadImage(
                context,
                leftDrawableUrl,
                16.toPx()
            ) {
                applyWhiteTintColorToVoucherFollowerImage(it)
                button.setDrawable(BitmapDrawable(context.resources, it))
            }
        } else {
            removeCompoundDrawableFollowButton()
        }
    }

    private fun applyWhiteTintColorToVoucherFollowerImage(voucherFollowerBitmap: Bitmap) {
        val tintColor = MethodChecker.getColor(
            context,
            unifyprinciplesR.color.Unify_Static_White
        ) // Replace with your desired tint color
        val canvas = Canvas(voucherFollowerBitmap)
        val paint = Paint()
        val filter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        paint.colorFilter = filter
        canvas.drawBitmap(voucherFollowerBitmap, 0f, 0f, paint)
    }

    private fun removeCompoundDrawableFollowButton() {
        val spannableText = SpannableString(buttonFollow?.text)
        val spanList = spannableText.getSpans(0, spannableText.length, ImageSpan::class.java)
        if (spanList.isNotEmpty()) {
            buttonFollow?.setDrawable(null)
        }
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
        isMyShop: Boolean = false
    ) {
        tickerShopStatus?.show()
        tickerShopStatus?.tickerTitle =
            HtmlLinkHelper(context, shopOperationalHourStatus.tickerTitle).spannedString.toString()
        setTickerInitialTextDescription(shopOperationalHourStatus.tickerMessage)
        tickerShopStatus?.setHtmlDescription(shopOperationalHourStatus.tickerMessage)
        tickerShopStatus?.tickerType = if (isMyShop) {
            Ticker.TYPE_WARNING
        } else {
            Ticker.TYPE_ANNOUNCEMENT
        }

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
        val htmlDescription  = if (shopStatus == ShopStatusDef.MODERATED && isMyShop) {
            generateShopModerateTickerDescription(statusMessage)
        } else {
            statusMessage
        }
        tickerShopStatus?.tickerTitle = MethodChecker.fromHtml(statusTitle).toString()
        setTickerInitialTextDescription(htmlDescription)
        tickerShopStatus?.setHtmlDescription(htmlDescription)
        tickerShopStatus?.show()
        tickerShopStatus?.tickerType = when (shopTickerType) {
            ShopTickerType.INFO -> Ticker.TYPE_ANNOUNCEMENT
            ShopTickerType.WARNING -> Ticker.TYPE_WARNING
            ShopTickerType.DANGER -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_WARNING
        }

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

    /**
     * TODO temporary fix, need to investigate further, since if we don't set the initial text description it will truncate the text
     */
    private fun setTickerInitialTextDescription(htmlDescription: String) {
        tickerShopStatus?.apply {
            val tickerDescription: TextView? = findViewById(unifycomponentsR.id.ticker_description)
            tickerDescription?.text = MethodChecker.fromHtml(htmlDescription)
        }
    }

    private fun doOnDelayFinished(delayMillis: Long, block: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            try {
                block()
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
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
        return if (coachMarkText.isNotBlank() && listenerHeader?.isFirstTimeVisit() == false && buttonFollowView != null) {
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

    private fun cycleDynamicUspText(dynamicUspValue: String) {
        if (dynamicUspValue.isNotEmpty()) {
            animateDynamicUspText(-textDynamicUspPerformance?.height?.toFloat().orZero())
                ?.withEndAction {
                    textDynamicUspPerformance?.translationY =
                        textDynamicUspPerformance?.height?.toFloat().orZero()
                    animateDynamicUspText(Float.ZERO)
                    updateDynamicUspValue(dynamicUspValue)
                }
        }
    }

    private fun animateDynamicUspText(translationY: Float): ViewPropertyAnimator? {
        return textDynamicUspPerformance?.animate()?.translationY(translationY)?.setDuration(UnifyMotion.T3)
    }

    private fun setHeaderBackgroundVideo(videoUrl: String) {
        backgroundImageShopHeader?.hide()
        if (playVideoWrapper == null) {
            playVideoWrapper = PlayVideoWrapper.Builder(context).build()
            playVideoWrapper?.addListener(object : PlayVideoWrapper.Listener {
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
            uri = Uri.parse(videoUrl)
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
        val listDynamicUspValue =
            listWidgetShopData.getDynamicUspComponent()?.text?.map { it.textHtml }.orEmpty()
        if (listDynamicUspValue.isNotEmpty()) {
            timer.startTimer(CYCLE_DURATION) {
                if (currentIndexUspDynamicValue == listDynamicUspValue.size - Int.ONE) {
                    currentIndexUspDynamicValue = Int.ZERO
                } else {
                    ++currentIndexUspDynamicValue
                }
                val currentValue = listDynamicUspValue[currentIndexUspDynamicValue]
                cycleDynamicUspText(currentValue)
            }
        }
    }

    fun clearTimerDynamicUsp() {
        timer.stopTimer()
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
        timer.stopTimer()
    }

    fun resumeTimerDynamicUspCycle() {
    }
}
