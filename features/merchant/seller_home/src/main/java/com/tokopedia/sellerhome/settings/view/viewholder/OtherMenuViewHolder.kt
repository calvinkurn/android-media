package com.tokopedia.sellerhome.settings.view.viewholder

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.PMProURL
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller.menu.common.analytics.*
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.sellerhome.settings.analytics.SettingShopOperationalTracker
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.ShopOperationalUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.lang.StringBuilder
import java.util.*

class OtherMenuViewHolder(private val itemView: View,
                          private val context: Context,
                          private val listener: Listener,
                          private val trackingListener: SettingTrackingListener,
                          private val freeShippingTracker: SettingFreeShippingTracker,
                          private val shopOperationalTracker: SettingShopOperationalTracker,
                          private val userSession: UserSessionInterface) {

    companion object {
        private val GREY_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N700_68

        private val TEAL_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_T500
        private val YELLOW_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_Y400

        private const val TAB_PM_PARAM = "tab"
        private const val TAB_PM = "pm"
        private const val TAB_PM_PRO = "pm_pro"
    }

    private var shopStatusHeader: AppCompatImageView? = null
    private var shopStatusHeaderIcon: AppCompatImageView? = null

    private var successLayout: Group? = null
    private var loadingLayout: Group? = null
    private var errorLayoutGroup: Group? = null
    private var errorLayout: ConstraintLayout? = null

    private var successHeaderGroup: Group? = null

    private var shopAvatarImage: ImageUnify? = null
    private var shopNameText: Typography? = null
    private var shopNextButton: AppCompatImageView? = null

    private var shopBadgeFollowersShimmer: LinearLayout? = null
    private var shopBadgeFollowersError: ConstraintLayout? = null
    private var shopBadgeFollowersDot: Typography? = null
    private var shopBadgeImage: AppCompatImageView? = null
    private var shopBadgeShimmer: LoaderTextView? = null
    private var shopBadgeErrorLayout: ConstraintLayout? = null
    private var shopFollowersText: Typography? = null
    private var shopFollowersShimmer: LoaderTextView? = null
    private var shopFollowersErrorLayout: ConstraintLayout? = null

    private var allErrorLocalLoad: LocalLoad? = null
    private var balanceErrorLocalLoad: LocalLoad? = null

    private var shopStatusContainer: LinearLayout? = null

    private var freeShippingLayout: FrameLayout? = null

    private var operationalHourView: View? = null
    private var operationalHourText: Typography? = null
    private var operationalHourLabel: Label? = null
    private var operationalHourImage: ImageView? = null
    private var operationalHourShimmer: LoaderTextView? = null
    private var operationalErrorLayout: ConstraintLayout? = null

    private var saldoLayout: Group? = null
    private var saldoShimmer: LoaderTextView? = null
    private var saldoBalanceText: Typography? = null
    private var saldoErrorLayout: ConstraintLayout? = null

    private var topAdsLayout: Group? = null
    private var topAdsShimmer: LoaderTextView? = null
    private var topAdsBalanceText: Typography? = null
    private var topAdsTooltipImage: AppCompatImageView? = null
    private var topAdsErrorLayout: ConstraintLayout? = null

    fun setupInitialLayout(shouldSetInitially: Boolean) {
        initLayoutComponents()

        if (shouldSetInitially) {
            shopNameText?.setShopName(userSession.shopName)
            shopAvatarImage?.setShopAvatar(ShopAvatarUiModel(userSession.shopAvatar))
        }
        setupShopNextButton()
    }

    private fun initLayoutComponents() {
        itemView.run {
            shopStatusHeader = findViewById(R.id.iv_sah_other_status_header)
            shopStatusHeaderIcon = findViewById(R.id.iv_sah_other_status_icon)

            successLayout = findViewById(R.id.group_sah_other_success)
            loadingLayout = findViewById(R.id.group_sah_other_loading)
            errorLayout = findViewById(R.id.shopInfoError)
            errorLayoutGroup = findViewById(R.id.group_sah_other_error)

            successHeaderGroup = findViewById(R.id.group_sah_other_header_success)

            shopAvatarImage = findViewById(R.id.shopImage)
            shopNameText = findViewById(R.id.shopName)
            shopNextButton = findViewById(R.id.settingShopNext)

            allErrorLocalLoad = findViewById(R.id.localLoadOthers)
            balanceErrorLocalLoad = findViewById(R.id.local_load_sah_others_balance)

            shopBadgeFollowersShimmer = findViewById(R.id.shimmer_sah_other_badge_followers)
            shopBadgeFollowersError = findViewById(R.id.layout_sah_other_badge_followers_error)
            shopBadgeFollowersDot = findViewById(R.id.dot)
            shopBadgeImage = findViewById(R.id.shopBadges)
            shopBadgeShimmer = findViewById(R.id.shimmer_sah_other_badge)
            shopBadgeErrorLayout = findViewById(R.id.layout_sah_other_badge_error)
            shopFollowersText = findViewById(R.id.shopFollowers)
            shopFollowersShimmer = findViewById(R.id.shimmer_sah_other_followers)
            shopFollowersErrorLayout = findViewById(R.id.layout_sah_other_followers_error)

            shopStatusContainer = findViewById(R.id.shopStatus)

            freeShippingLayout = findViewById(R.id.freeShippingLayout)

            operationalHourView = findViewById(R.id.shopOperationalHour)
            operationalHourText = findViewById(R.id.textOperationalHour)
            operationalHourLabel = findViewById(R.id.labelShopStatus)
            operationalHourImage = findViewById(R.id.imageOperationalHour)
            operationalHourShimmer = findViewById(R.id.shimmer_sah_other_shop_operational)
            operationalErrorLayout = findViewById(R.id.layout_sah_other_operational_failed)

            saldoLayout = findViewById(R.id.group_sah_other_saldo)
            saldoShimmer = findViewById(R.id.shimmer_shc_balance)
            saldoBalanceText = findViewById(R.id.balanceValue)
            saldoErrorLayout = findViewById(R.id.layout_shc_other_balance_failed)

            topAdsLayout = findViewById(R.id.group_sah_other_topads)
            topAdsShimmer = findViewById(R.id.shimmer_sah_other_topads)
            topAdsBalanceText = findViewById(R.id.tv_sah_other_topads_balance)
            topAdsTooltipImage = findViewById(R.id.iv_sah_other_topads_tooltip)
            topAdsErrorLayout = findViewById(R.id.layout_sah_other_topads_failed)
        }
    }
    
    fun onSuccessGetSettingShopInfoData(uiModel: SettingShopInfoUiModel) {
        with(uiModel) {
            when {
                partialResponseStatus.first && partialResponseStatus.second -> {
                    setupSuccessLayout()
                    shopStatusUiModel?.let { setShopStatusType(it) }
                    saldoBalanceUiModel?.let { setSaldoBalance(it) }
                    topadsBalanceUiModel?.let { setKreditTopadsBalance(it) }
                    shopBadgeUiModel?.let { setShopBadge(it) }
                    shopFollowersUiModel?.let {
                        setShopTotalFollowers(it)
                        setDotVisibility(it.shopFollowers)
                    }

                    allErrorLocalLoad?.gone()
                    balanceErrorLocalLoad?.gone()
                    shopStatusContainer?.visible()
                    saldoLayout?.visible()
                    topAdsLayout?.visible()
                }
                partialResponseStatus.first -> {
                    setupSuccessLayout()
                    shopStatusUiModel?.let { setShopStatusType(it) }
                    shopBadgeUiModel?.let { setShopBadge(it) }
                    shopFollowersUiModel?.let {
                        setShopTotalFollowers(it)
                        setDotVisibility(it.shopFollowers)
                    }

                    shopStatusContainer?.visible()
                    allErrorLocalLoad?.gone()
                    balanceErrorLocalLoad?.run {
                        setup()
                        visible()
                    }
                    saldoLayout?.gone()
                    topAdsLayout?.gone()
                }
                partialResponseStatus.second -> {
                    setupSuccessLayout()
                    saldoBalanceUiModel?.let { setSaldoBalance(it) }
                    topadsBalanceUiModel?.let { setKreditTopadsBalance(it) }

                    shopBadgeFollowersDot?.gone()
                    shopStatusContainer?.gone()
                    allErrorLocalLoad?.run {
                        setup()
                        visible()
                    }
                    balanceErrorLocalLoad?.gone()
                    saldoLayout?.visible()
                    topAdsLayout?.visible()
                }
                else -> {
                    onErrorGetSettingShopInfoData()
                }
            }
        }
    }

    fun onLoadingGetSettingShopInfoData() {
        itemView.run {
            loadingLayout?.visible()
            successLayout?.gone()
            errorLayoutGroup?.gone()
        }
    }

    fun onErrorGetSettingShopInfoData() {
        itemView.run {
            loadingLayout?.gone()
            successLayout?.gone()
            errorLayoutGroup?.visible()
            errorLayout?.run {
                findViewById<Typography>(R.id.dot)?.gone()
                findViewById<LocalLoad>(R.id.settingLocalLoad)?.setup()
                findViewById<Typography>(R.id.shopName)?.setShopName(userSession.shopName)
                findViewById<ImageUnify>(R.id.shopImage)?.setShopAvatar(ShopAvatarUiModel(userSession.shopAvatar))
                setOnClickAction()
            }
        }
    }

    fun setBadgeFollowersLoading(isLoading: Boolean) {
        shopBadgeFollowersShimmer?.showWithCondition(isLoading)
        if (isLoading) {
            hideBadgeFollowersLayout()
            shopBadgeFollowersError?.gone()
        }
    }

    fun setBadgeFollowersError(isError: Boolean) {
        shopBadgeFollowersError?.run {
            showWithCondition(isError)
            setOnClickListener {
                listener.onRefreshData()
                listener.onShopBadgeFollowersRefresh()
            }
        }
        if (isError) {
            hideBadgeFollowersLayout()
            shopBadgeFollowersShimmer?.gone()
        }
    }

    fun setShopBadge(shopBadgeUiModel: ShopBadgeUiModel) {
        shopBadgeShimmer?.gone()
        shopBadgeErrorLayout?.gone()
        shopBadgeFollowersDot?.visible()
        shopBadgeImage?.run {
            ImageHandler.LoadImage(this, shopBadgeUiModel.shopBadgeUrl)
            setOnClickListener {
                listener.onShopBadgeClicked()
                shopBadgeUiModel.sendSettingShopInfoClickTracking()
            }
            show()
        }
    }

    fun setShopBadgeLoading() {
        shopBadgeShimmer?.show()
        shopBadgeImage?.gone()
        shopBadgeErrorLayout?.gone()
        shopBadgeFollowersDot?.visible()
    }

    fun setShopBadgeError() {
        shopBadgeErrorLayout?.run {
            show()
            setOnClickListener {
                listener.onRefreshData()
                listener.onShopBadgeRefresh()
            }
        }
        shopBadgeImage?.gone()
        shopBadgeShimmer?.gone()
        shopBadgeFollowersDot?.visible()
    }

    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
        shopFollowersShimmer?.gone()
        shopFollowersErrorLayout?.gone()
        shopBadgeFollowersDot?.visible()
        val shouldShowFollowers = shopTotalFollowersUiModel.shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val followersVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        shopFollowersText?.run {
            visibility = followersVisibility
            text = StringBuilder("${shopTotalFollowersUiModel.shopFollowers} ${context.resources.getString(R.string.setting_followers)}")
            setOnClickListener {
                shopTotalFollowersUiModel.sendSettingShopInfoClickTracking()
                listener.onFollowersCountClicked()
            }
            show()
        }
    }

    fun setShopTotalFollowersLoading() {
        shopFollowersShimmer?.show()
        shopFollowersText?.gone()
        shopFollowersErrorLayout?.gone()
        shopBadgeFollowersDot?.visible()
    }

    fun setShopTotalFollowersError() {
        shopFollowersErrorLayout?.run {
            show()
            setOnClickListener {
                listener.onRefreshData()
                listener.onShopTotalFollowersRefresh()
            }
        }
        shopFollowersText?.gone()
        shopFollowersShimmer?.gone()
        shopBadgeFollowersDot?.visible()
    }

    fun setupFreeShippingLayout() {
        freeShippingLayout?.run {
            setOnClickListener {
                listener.onFreeShippingClicked()
                freeShippingTracker.trackFreeShippingClick()
            }
            visibility = View.VISIBLE

            freeShippingTracker.trackFreeShippingImpression()
        }
    }

    fun hideFreeShippingLayout() {
        freeShippingLayout?.hide()
    }

    fun showOperationalHourLayout(shopOperational: ShopOperationalUiModel) {
        setupOperationalHourText(shopOperational)

        val shopOperationalStatus = itemView.context.getString(shopOperational.status)
        setupOperationalHourLabel(shopOperationalStatus, shopOperational)
        setupOperationalHourClick(shopOperationalStatus, shopOperational)
        setupOperationalHourImage(shopOperational)

        operationalHourShimmer?.gone()
        operationalErrorLayout?.gone()
    }

    private fun hideBadgeFollowersLayout() {
        shopBadgeErrorLayout?.gone()
        shopFollowersErrorLayout?.gone()
        shopBadgeImage?.gone()
        shopBadgeFollowersDot?.gone()
        shopFollowersText?.gone()
    }

    private fun setupOperationalHourText(shopOperational: ShopOperationalUiModel) {
        val timeLabel = shopOperational.timeLabel
        operationalHourText?.run {
            text = if(timeLabel != null) {
                context.getString(timeLabel)
            } else {
                shopOperational.time
            }
            show()
        }
    }

    private fun setupOperationalHourLabel(shopOperationalStatus: String, shopOperational: ShopOperationalUiModel) {
        operationalHourLabel?.run {
            text = shopOperationalStatus
            setLabelType(shopOperational.labelType)
            show()
        }
    }

    private fun setupOperationalHourClick(shopOperationalStatus: String, shopOperational: ShopOperationalUiModel) {
        if (shopOperational.hasShopSettingsAccess) {
            operationalHourView?.run {
                setOnClickListener {
                    shopOperationalTracker.trackClickShopOperationalHour(shopOperationalStatus)
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE)
                }
            }
        }
    }

    private fun setupOperationalHourImage(shopOperational: ShopOperationalUiModel) {
        operationalHourImage?.run {
            setImageDrawable(ContextCompat.getDrawable(context, shopOperational.icon))
            show()
        }
    }

    fun showOperationalHourLayoutLoading() {
        operationalHourShimmer?.show()
        operationalErrorLayout?.gone()
        operationalHourText?.gone()
        operationalHourImage?.gone()
        operationalHourLabel?.gone()
    }

    fun showOperationalHourLayoutError() {
        operationalErrorLayout?.run {
            show()
            setOnClickListener {
                listener.onRefreshData()
                listener.onOperationalHourRefresh()
            }
        }
        operationalHourShimmer?.gone()
        operationalHourText?.gone()
        operationalHourImage?.gone()
        operationalHourLabel?.gone()
    }

    private fun setDotVisibility(shopFollowers: Long) {
        val shouldShowFollowers = shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val dotVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        shopBadgeFollowersDot?.visibility = dotVisibility
    }

    private fun setupSuccessLayout() {
        loadingLayout?.gone()
        errorLayoutGroup?.gone()
        successLayout?.visible()
        shopNameText?.setShopName(userSession.shopName)
        shopAvatarImage?.setShopAvatar(ShopAvatarUiModel(userSession.shopAvatar))
    }

    private fun Typography.setShopName(shopName: String) {
        text = MethodChecker.fromHtml(shopName)
        setOnClickListener {
            listener.onShopInfoClicked()
            sendClickShopNameTracking()
        }
    }

    private fun ImageUnify.setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        urlSrc = shopAvatarUiModel.shopAvatarUrl
        sendSettingShopInfoImpressionTracking(shopAvatarUiModel, trackingListener::sendImpressionDataIris)
        setOnClickListener {
            listener.onShopInfoClicked()
            shopAvatarUiModel.sendSettingShopInfoClickTracking()
        }
    }

    private fun setupShopNextButton() {
        shopNextButton?.setOnClickListener {
            listener.onShopInfoClicked()
            sendShopInfoClickNextButtonTracking()
        }
    }

    fun setSaldoBalance(saldoBalanceUiModel: BalanceUiModel) {
        saldoBalanceText?.run {
            text = saldoBalanceUiModel.balanceValue
            setOnClickListener {
                listener.onSaldoClicked()
                saldoBalanceUiModel.sendSettingShopInfoClickTracking()
            }
            show()
        }
        saldoShimmer?.gone()
        saldoErrorLayout?.gone()
        saldoLayout?.sendSettingShopInfoImpressionTracking(saldoBalanceUiModel, trackingListener::sendImpressionDataIris)
    }

    fun setSaldoBalanceLoading() {
        saldoShimmer?.show()
        saldoBalanceText?.gone()
        saldoErrorLayout?.gone()
    }

    fun setSaldoBalanceError() {
        saldoErrorLayout?.run {
            show()
            setOnClickListener {
                listener.onRefreshData()
                listener.onSaldoBalanceRefresh()
            }
        }
        saldoShimmer?.gone()
        saldoBalanceText?.gone()
    }

    fun setKreditTopadsBalance(topadsBalanceUiModel: TopadsBalanceUiModel) {
        topAdsLayout?.sendSettingShopInfoImpressionTracking(topadsBalanceUiModel, trackingListener::sendImpressionDataIris)
        setupKreditTopadsBalanceText(topadsBalanceUiModel)
        topAdsBalanceText?.show()
        topAdsShimmer?.gone()
        topAdsErrorLayout?.gone()
    }

    private fun setupKreditTopadsBalanceText(topadsBalanceUiModel: TopadsBalanceUiModel) {
        topAdsBalanceText?.run {
            text = topadsBalanceUiModel.balanceValue
            setOnClickListener {
                listener.onKreditTopadsClicked()
                topadsBalanceUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    fun setupKreditTopadsBalanceTooltip(isTopAdsUser: Boolean?) {
        if (isTopAdsUser == null) {
            topAdsTooltipImage?.gone()
        } else {
            val topAdsTooltipDrawable =
                    if (isTopAdsUser) {
                        ContextCompat.getDrawable(context, R.drawable.ic_topads_active)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_topads_inactive)
                    }
            topAdsTooltipImage?.run {
                setImageDrawable(topAdsTooltipDrawable)
                setOnClickListener {
                    listener.onTopAdsTooltipClicked(isTopAdsUser)
                }
                show()
            }
        }
    }

    fun setKreditTopadsBalanceLoading() {
        topAdsShimmer?.show()
        topAdsBalanceText?.gone()
        topAdsErrorLayout?.gone()
    }

    fun setKreditTopadsBalanceError() {
        topAdsErrorLayout?.run {
            show()
            setOnClickListener {
                listener.onRefreshData()
                listener.onKreditTopAdsRefresh()
            }
        }
        topAdsShimmer?.gone()
        topAdsBalanceText?.gone()
    }

    fun setShopStatusError() {
        shopStatusContainer?.run {
            val shopStatusLayout = LayoutInflater.from(context).inflate(R.layout.view_sah_shop_status_error, this, false)?.apply {
                setOnClickListener {
                    listener.onRefreshData()
                    listener.onUserInfoRefresh()
                }
            }
            removeAllViews()
            shopStatusLayout?.let { view ->
                addView(view)
            }
        }
    }

    fun setShopStatusLoading() {
        shopStatusContainer?.run {
            val shopStatusLayout = LayoutInflater.from(context).inflate(R.layout.view_sah_shop_status_loading, this, false)
            removeAllViews()
            shopStatusLayout?.let { view ->
                addView(view)
            }
        }
    }

    fun setShopStatusType(shopStatusUiModel: ShopStatusUiModel) {
        val shopType = shopStatusUiModel.userShopInfoWrapper.shopType
        showShopStatusHeader(shopType)
        val layoutInflater = shopType?.shopTypeLayoutRes?.let { LayoutInflater.from(context).inflate(it, null, false) }
        val shopStatusLayout: View? = when(shopType) {
            is RegularMerchant -> {
                listener.onStatusBarNeedDarkColor(true)
                layoutInflater?.apply {
                    setRegularMerchantShopStatus(shopType, shopStatusUiModel)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM)
                    }
                }
            }
            is PowerMerchantStatus -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater?.apply {
                    setPowerMerchantShopStatus(shopType, shopStatusUiModel)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                    }
                }
            }
            is ShopType.OfficialStore -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater?.apply {
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                }
            }
            is PowerMerchantProStatus.Advanced -> {
                layoutInflater?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                    }
                }

            }
            is PowerMerchantProStatus.Expert -> {
                layoutInflater?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                    }
                }
            }
            is PowerMerchantProStatus.Ultimate -> {
                layoutInflater?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                    }
                }
            }
            is PowerMerchantProStatus.InActive -> {
                layoutInflater?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                    }
                }
            }
            else -> null
        }
        shopStatusContainer?.run {
            removeAllViews()
            shopStatusLayout?.let { view ->
                addView(view)
            }
        }
    }

    fun setAllErrorLocalLoad(isAllError: Boolean) {
        allErrorLocalLoad?.run {
            if (isAllError) {
                setup()
            }
            showWithCondition(isAllError)
        }
        successHeaderGroup?.showWithCondition(!isAllError)
    }

    private fun goToPowerMerchantSubscribe(tab: String) {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val appLinkPMTab = Uri.parse(appLink).buildUpon().appendQueryParameter(TAB_PM_PARAM, tab).build().toString()
        context.let { RouteManager.route(context, appLinkPMTab) }
    }

    private fun View.hideTransactionSection() {
        findViewById<View>(com.tokopedia.seller.menu.common.R.id.divider_stats_rm)?.hide()
        findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.tx_stats_rm)?.hide()
        findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.tx_total_stats_rm)?.hide()
    }

    private fun View.showTransactionSection() {
        findViewById<View>(com.tokopedia.seller.menu.common.R.id.divider_stats_rm)?.show()
        findViewById<View>(com.tokopedia.seller.menu.common.R.id.divider_stats_rm)?.setBackgroundResource(com.tokopedia.seller.menu.common.R.drawable.ic_divider_stats_rm)
        findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.tx_stats_rm)?.show()
        findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.tx_total_stats_rm)?.show()
    }

    private fun showShopStatusHeader(shopType: ShopType?) {
        shopType?.let { shopStatusHeader?.setImageDrawable(ContextCompat.getDrawable(context, it.shopTypeHeaderRes)) }
        shopStatusHeaderIcon?.run {
            if (shopType !is RegularMerchant) {
                visibility = View.VISIBLE
                shopType?.shopTypeHeaderIconRes?.let { iconRes ->
                    setImageDrawable(ContextCompat.getDrawable(context, iconRes))
                }
            } else {
                visibility = View.GONE
            }
        }
    }

    private fun View.setPowerMerchantShopStatus(powerMerchantStatus: PowerMerchantStatus, statusUiModel: ShopStatusUiModel): View {
        val upgradePMTextView: Typography = findViewById(com.tokopedia.seller.menu.common.R.id.upgradePMText)
        val powerMerchantStatusTextView: Typography = findViewById(com.tokopedia.seller.menu.common.R.id.powerMerchantStatusText)
        val powerMerchantText: Typography = findViewById(com.tokopedia.seller.menu.common.R.id.powerMerchantText)
        val periodType = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.periodTypePmPro
        val isNewSeller = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.isNewSeller
        when (powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                if (periodType == Constant.D_DAY_PERIOD_TYPE_PM_PRO) {
                    upgradePMTextView.showWithCondition(isNewSeller == false)
                } else if (periodType == Constant.COMMUNICATION_PERIOD_PM_PRO) {
                    upgradePMTextView.hide()
                }
                powerMerchantStatusTextView.hide()
                powerMerchantText.text = context?.getString(com.tokopedia.seller.menu.common.R.string.power_merchant_upgrade)
            }
            is PowerMerchantStatus.NotActive -> {
                powerMerchantStatusTextView.show()
                upgradePMTextView.hide()
                powerMerchantText.text = context?.getString(com.tokopedia.seller.menu.common.R.string.power_merchant_status)
                powerMerchantStatusTextView.setOnClickListener {
                    goToPowerMerchantSubscribe(TAB_PM_PRO)
                }
            }
        }
        return this
    }

    private fun View.setRegularMerchantShopStatus(regularMerchant: RegularMerchant, shopStatusUiModel: ShopStatusUiModel): View {
        val userShopInfo = shopStatusUiModel.userShopInfoWrapper.userShopInfoUiModel
        val txStatsRM = findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.tx_stats_rm)
        val txTotalStatsRM = findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.tx_total_stats_rm)
        val regularMerchantStatus = findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.regularMerchantStatus)
        regularMerchantStatus.run {
            text = when (regularMerchant) {
                is RegularMerchant.NeedUpgrade -> context.resources.getString(com.tokopedia.seller.menu.common.R.string.setting_upgrade)
            }
        }

        val thresholdTransaction  = 110
        val maxTransaction = 100
        val totalTransaction = userShopInfo?.totalTransaction ?: 0
        if (totalTransaction >= thresholdTransaction) {
            hideTransactionSection()
        } else {
            if (userShopInfo?.periodTypePmPro == Constant.D_DAY_PERIOD_TYPE_PM_PRO) {
                showTransactionSection()
                if (totalTransaction > maxTransaction) {
                    txStatsRM.text = MethodChecker.fromHtml(context?.getString(com.tokopedia.seller.menu.common.R.string.transaction_passed))
                    txTotalStatsRM.hide()
                } else {
                    txStatsRM.setupStatsWordingRM(userShopInfo)
                    txTotalStatsRM.show()
                    txTotalStatsRM.text = context?.getString(com.tokopedia.seller.menu.common.R.string.total_transaction, totalTransaction.toString())
                }
            } else {
                hideTransactionSection()
            }
        }
        return this
    }

    private fun Typography.setupStatsWordingRM(userShopInfo: UserShopInfoWrapper.UserShopInfoUiModel) {
        text = if(userShopInfo.dateCreated.isBlank()) {
            context?.getString(com.tokopedia.seller.menu.common.R.string.transaction_on_date)
        } else {
            if (userShopInfo.isBeforeOnDate) {
                context?.getString(com.tokopedia.seller.menu.common.R.string.transaction_on_date)
            } else {
                context?.getString(com.tokopedia.seller.menu.common.R.string.transaction_since_joining)
            }
        }
    }

    private fun View.setPowerMerchantProStatus(shopStatusUiModel: ShopStatusUiModel, powerMerchantStatus: PowerMerchantProStatus): View {
        val goldOS = shopStatusUiModel.userShopInfoWrapper.userShopInfoUiModel
        val ivBgPMPro = findViewById<ShapeableImageView>(com.tokopedia.seller.menu.common.R.id.iv_bg_pm_pro)
        val powerMerchantProIcon = findViewById<IconUnify>(com.tokopedia.seller.menu.common.R.id.powerMerchantProIcon)
        val powerMerchantProStatusText = findViewById<Typography>(com.tokopedia.seller.menu.common.R.id.powerMerchantProStatusText)
        when (powerMerchantStatus) {
            is PowerMerchantProStatus.Advanced -> {
                ivBgPMPro.loadImage(PMProURL.BG_ADVANCE)
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, GREY_TEXT_COLOR))
                powerMerchantProStatusText.text = goldOS?.pmProGradeName?.capitalize(Locale.getDefault()) ?: ""
            }
            is PowerMerchantProStatus.Expert -> {
                ivBgPMPro.loadImage(PMProURL.BG_EXPERT)
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, TEAL_TEXT_COLOR))
                powerMerchantProStatusText.text = goldOS?.pmProGradeName?.capitalize(Locale.getDefault()) ?: ""
            }
            is PowerMerchantProStatus.Ultimate -> {
                ivBgPMPro.loadImage(PMProURL.BG_ULTIMATE)
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, YELLOW_TEXT_COLOR))
                powerMerchantProStatusText.text = goldOS?.pmProGradeName?.capitalize(Locale.getDefault()) ?: ""
            }
            is PowerMerchantProStatus.InActive -> {
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                powerMerchantProStatusText.text = context?.getString(com.tokopedia.seller.menu.common.R.string.setting_not_active)
            }
        }
        val roundedRadius = 16F
        ivBgPMPro.shapeAppearanceModel = ivBgPMPro.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, roundedRadius)
                .build()
        powerMerchantProIcon.loadImage(if (goldOS?.badge?.isBlank() == true) PMProURL.ICON_URL else goldOS?.badge)
        return this
    }

    private fun View.setOnClickAction() {
        findViewById<AppCompatImageView>(R.id.settingShopNext)?.setOnClickListener {
            listener.onShopInfoClicked()
            sendShopInfoClickNextButtonTracking()
        }
    }

    private fun LocalLoad.setup() {
        title?.text = context.resources.getString(R.string.setting_error_message)
        description?.text = context.resources.getString(R.string.setting_error_description)
        refreshBtn?.setOnClickListener {
            listener.onRefreshShopInfo()
        }
    }

    interface Listener {
        fun onShopInfoClicked()
        fun onShopBadgeClicked()
        fun onFollowersCountClicked()
        fun onSaldoClicked()
        fun onKreditTopadsClicked()
        fun onRefreshShopInfo()
        fun onStatusBarNeedDarkColor(isDefaultDark: Boolean)
        fun onTopAdsTooltipClicked(isTopAdsActive: Boolean)
        fun onFreeShippingClicked()
        fun onRefreshData()
        fun onShopBadgeRefresh()
        fun onShopTotalFollowersRefresh()
        fun onShopBadgeFollowersRefresh()
        fun onUserInfoRefresh()
        fun onOperationalHourRefresh()
        fun onSaldoBalanceRefresh()
        fun onKreditTopAdsRefresh()
    }

}