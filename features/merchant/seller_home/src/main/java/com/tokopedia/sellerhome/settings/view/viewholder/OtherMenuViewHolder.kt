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
import kotlinx.android.synthetic.main.fragment_other_menu.view.*
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

    private var shopAvatarImage: ImageUnify? = null
    private var shopNameText: Typography? = null
    private var shopNextButton: AppCompatImageView? = null

    private var shopBadgeFollowersGroup: Group? = null
    private var shopBadgeFollowersShimmer: LoaderTextView? = null
    private var shopBadgeFollowersDot: Typography? = null
    private var shopBadgeImage: AppCompatImageView? = null
    private var shopBadgeShimmer: LoaderTextView? = null
    private var shopBadgeErrorGroup: Group? = null
    private var shopFollowersText: Typography? = null
    private var shopFollowersShimmer: LoaderTextView? = null
    private var shopFollowersErrorGroup: Group? = null

    private var errorLocalLoad: LocalLoad? = null

    private var shopStatusContainer: LinearLayout? = null

    private var freeShippingLayout: FrameLayout? = null

    private var operationalHourView: View? = null
    private var operationalHourText: Typography? = null
    private var operationalHourLabel: Label? = null
    private var operationalHourImage: ImageView? = null
    private var operationalHourShimmer: LoaderTextView? = null
    private var operationalErrorGroup: Group? = null

    private var saldoLayout: LinearLayout? = null
    private var saldoShimmer: LoaderTextView? = null
    private var saldoBalanceText: Typography? = null
    private var saldoErrorGroup: Group? = null

    private var topAdsLayout: ConstraintLayout? = null
    private var topAdsShimmer: LoaderTextView? = null
    private var topAdsBalanceText: Typography? = null
    private var topAdsTooltipImage: AppCompatImageView? = null
    private var topAdsErrorGroup: Group? = null

    fun setupInitialLayout() {
        initLayoutComponents()

        setShopName(userSession.shopName)
        setShopAvatar(ShopAvatarUiModel(userSession.shopAvatar))
        setupShopNextButton()
    }

    private fun initLayoutComponents() {
        itemView.run {
            shopAvatarImage = findViewById(R.id.shopImage)
            shopNameText = findViewById(R.id.shopName)
            shopNextButton = findViewById(R.id.settingShopNext)

            errorLocalLoad = findViewById(R.id.localLoadOthers)

            shopBadgeFollowersGroup = findViewById(R.id.group_sah_other_badge_followers)
            shopBadgeFollowersShimmer = findViewById(R.id.shimmer_sah_other_badge_followers)
            shopBadgeFollowersDot = findViewById(R.id.dot)
            shopBadgeImage = findViewById(R.id.shopBadges)
            shopBadgeShimmer = findViewById(R.id.shimmer_sah_other_badge)
            shopBadgeErrorGroup = findViewById(R.id.group_sah_other_badge_error)
            shopFollowersText = findViewById(R.id.shopFollowers)
            shopFollowersShimmer = findViewById(R.id.shimmer_sah_other_followers)
            shopFollowersErrorGroup = findViewById(R.id.group_sah_other_followers_error)

            shopStatusContainer = findViewById(R.id.shopStatus)

            freeShippingLayout = findViewById(R.id.freeShippingLayout)

            operationalHourView = findViewById(R.id.shopOperationalHour)
            operationalHourText = findViewById(R.id.textOperationalHour)
            operationalHourLabel = findViewById(R.id.labelShopStatus)
            operationalHourImage = findViewById(R.id.imageOperationalHour)
            operationalHourShimmer = findViewById(R.id.shimmer_sah_other_shop_operational)
            operationalErrorGroup = findViewById(R.id.group_sah_other_operational_failed)

            saldoLayout = findViewById(R.id.layout_sah_other_saldo)
            saldoShimmer = findViewById(R.id.shimmeringBalanceValue)
            saldoBalanceText = findViewById(R.id.balanceValue)
            saldoErrorGroup = findViewById(R.id.group_shc_balance_failed)

            topAdsLayout = findViewById(R.id.layout_sah_other_topads)
            topAdsShimmer = findViewById(R.id.shimmer_sah_other_topads)
            topAdsBalanceText = findViewById(R.id.tv_sah_other_topads_balance)
            topAdsTooltipImage = findViewById(R.id.iv_sah_other_topads_tooltip)
            topAdsErrorGroup = findViewById(R.id.group_sah_other_topads_failed)
        }
    }

    fun onSuccessGetSettingShopInfoData(uiModel: SettingShopInfoUiModel) {
        with(uiModel) {
            with(itemView) {
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

                        findViewById<LocalLoad>(R.id.localLoadOthers)?.gone()
                        findViewById<LinearLayout>(R.id.shopStatus)?.visible()
                        findViewById<LinearLayout>(R.id.layout_sah_other_saldo)?.visible()
                        findViewById<LinearLayout>(R.id.layout_sah_other_topads)?.visible()
                    }
                    partialResponseStatus.first -> {
                        setupSuccessLayout()
                        shopStatusUiModel?.let { setShopStatusType(it) }
                        shopBadgeUiModel?.let { setShopBadge(it) }
                        shopFollowersUiModel?.let {
                            setShopTotalFollowers(it)
                            setDotVisibility(it.shopFollowers)
                        }

                        findViewById<LinearLayout>(R.id.shopStatus)?.visible()
                        findViewById<LocalLoad>(R.id.localLoadOthers)?.run {
                            setup()
                            visible()
                        }
                        findViewById<LinearLayout>(R.id.layout_sah_other_saldo)?.gone()
                        findViewById<LinearLayout>(R.id.layout_sah_other_topads)?.gone()
                    }
                    partialResponseStatus.second -> {
                        setupSuccessLayout()
                        saldoBalanceUiModel?.let { setSaldoBalance(it) }
                        topadsBalanceUiModel?.let { setKreditTopadsBalance(it) }

                        findViewById<Typography>(R.id.dot)?.gone()
                        findViewById<LinearLayout>(R.id.shopStatus)?.gone()
                        findViewById<LocalLoad>(R.id.localLoadOthers)?.run {
                            setup()
                            visible()
                        }
                        findViewById<LinearLayout>(R.id.layout_sah_other_saldo)?.visible()
                        findViewById<LinearLayout>(R.id.layout_sah_other_topads)?.visible()
                    }
                    else -> {
                        onErrorGetSettingShopInfoData()
                    }
                }
            }
        }
    }

    fun onLoadingGetSettingShopInfoData() {
        val loadingLayout = LayoutInflater.from(context).inflate(R.layout.setting_partial_shop_info_loading, null, false)
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(loadingLayout)
        }
    }

    fun onErrorGetSettingShopInfoData() {
        val errorLayout = LayoutInflater.from(context).inflate(R.layout.setting_partial_shop_info_error, null, false)
        errorLayout?.run {
            findViewById<LocalLoad>(R.id.settingLocalLoad)?.setup()
            findViewById<Typography>(R.id.dot)?.gone()
        }
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(errorLayout)
        }
    }

    fun setBadgeFollowersLoading(isLoading: Boolean) {
        shopBadgeFollowersShimmer?.showWithCondition(isLoading)
        shopBadgeFollowersGroup?.showWithCondition(!isLoading)
    }

    fun setShopBadge(shopBadgeUiModel: ShopBadgeUiModel) {
        shopBadgeImage?.run {
            ImageHandler.LoadImage(this, shopBadgeUiModel.shopBadgeUrl)
            setOnClickListener {
                listener.onShopBadgeClicked()
                shopBadgeUiModel.sendSettingShopInfoClickTracking()
            }
        }
        shopBadgeShimmer?.gone()
        shopBadgeErrorGroup?.gone()
    }

    fun setShopBadgeLoading() {
        shopBadgeShimmer?.show()
        shopBadgeImage?.gone()
        shopBadgeErrorGroup?.gone()
    }

    fun setShopBadgeError() {
        shopBadgeErrorGroup?.run {
            show()
            setOnClickListener {
                // TODO: Reload data
            }
        }
        shopBadgeImage?.gone()
        shopBadgeShimmer?.gone()
    }

    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
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
        shopFollowersShimmer?.gone()
        shopFollowersErrorGroup?.gone()
    }

    fun setShopTotalFollowersLoading() {
        shopFollowersShimmer?.show()
        shopFollowersText?.gone()
        shopFollowersErrorGroup?.gone()
    }

    fun setShopTotalFollowersError() {
        shopFollowersErrorGroup?.run {
            show()
            setOnClickListener {
                // TODO: Refresh data
            }
        }
        shopFollowersText?.gone()
        shopFollowersShimmer?.gone()
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

        operationalHourShimmer?.hide()
        operationalErrorGroup?.hide()
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
        operationalErrorGroup?.gone()
        operationalHourImage?.gone()
        operationalHourLabel?.gone()
    }

    fun showOperationalHourLayoutError() {
        operationalErrorGroup?.run {
            show()
            setOnClickListener {
                // TODO: Refresh data
            }
        }
        operationalHourShimmer?.gone()
        operationalHourImage?.gone()
        operationalHourLabel?.gone()
    }

    private fun setDotVisibility(shopFollowers: Long) {
        val shouldShowFollowers = shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val dotVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        val tvDot = itemView.shopInfoLayout.findViewById<Typography>(R.id.dot)
        tvDot?.visibility = dotVisibility
    }

    private fun setupSuccessLayout() {
        val successLayout = LayoutInflater.from(context).inflate(R.layout.view_sah_other_header, null, false)
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(successLayout)
        }
    }

    private fun setShopName(shopName: String) {
        shopNameText?.run {
            text = MethodChecker.fromHtml(shopName)
            setOnClickListener {
                listener.onShopInfoClicked()
                sendClickShopNameTracking()
            }
        }
    }

    private fun setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        shopAvatarImage?.run {
            urlSrc = shopAvatarUiModel.shopAvatarUrl
            sendSettingShopInfoImpressionTracking(shopAvatarUiModel, trackingListener::sendImpressionDataIris)
            setOnClickListener {
                listener.onShopInfoClicked()
                shopAvatarUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setupShopNextButton() {
        shopNextButton?.setOnClickListener {
            listener.onShopInfoClicked()
            sendShopInfoClickNextButtonTracking()
        }
    }

    private fun setSaldoBalance(saldoBalanceUiModel: BalanceUiModel) {
        saldoBalanceText?.run {
            text = saldoBalanceUiModel.balanceValue
            setOnClickListener {
                listener.onSaldoClicked()
                saldoBalanceUiModel.sendSettingShopInfoClickTracking()
            }
            show()
        }
        saldoShimmer?.gone()
        saldoErrorGroup?.gone()
        saldoLayout?.sendSettingShopInfoImpressionTracking(saldoBalanceUiModel, trackingListener::sendImpressionDataIris)
    }

    private fun setSaldoBalanceLoading() {
        saldoShimmer?.show()
        saldoBalanceText?.gone()
        saldoErrorGroup?.gone()
    }

    private fun setSaldoBalanceError() {
        saldoErrorGroup?.run {
            show()
            setOnClickListener {
                // TODO: Refresh data
            }
        }
        saldoShimmer?.gone()
        saldoBalanceText?.gone()
    }

    private fun setKreditTopadsBalance(topadsBalanceUiModel: TopadsBalanceUiModel) {
        topAdsLayout?.sendSettingShopInfoImpressionTracking(topadsBalanceUiModel, trackingListener::sendImpressionDataIris)
        setupKreditTopadsBalanceText(topadsBalanceUiModel)
        setupKreditTopadsBalanceTooltip(topadsBalanceUiModel.isTopAdsUser)
        topAdsBalanceText?.show()
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

    private fun setupKreditTopadsBalanceTooltip(isTopAdsUser: Boolean) {
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
        }
    }

    private fun setKreditTopadsBalanceLoading() {
        topAdsShimmer?.show()
        topAdsTooltipImage?.gone()
        topAdsBalanceText?.gone()
        topAdsErrorGroup?.gone()
    }

    private fun setKreditTopadsBalanceError() {
        topAdsErrorGroup?.run {
            show()
            setOnClickListener {
                // TODO: Refresh data
            }
        }
        topAdsShimmer?.gone()
        topAdsTooltipImage?.gone()
        topAdsBalanceText?.gone()
    }

    private fun setShopStatusError() {
        shopStatusContainer?.run {
            val shopStatusLayout = LayoutInflater.from(context).inflate(R.layout.view_sah_shop_status_error, this, false)?.apply {
                setOnClickListener {
                    // TODO: Reload data
                }
            }
            removeAllViews()
            shopStatusLayout?.let { view ->
                addView(view)
            }
        }
    }

    private fun setShopStatusLoading() {
        shopStatusContainer?.run {
            val shopStatusLayout = LayoutInflater.from(context).inflate(R.layout.view_sah_shop_status_loading, this, false)
            removeAllViews()
            shopStatusLayout?.let { view ->
                addView(view)
            }
        }
    }

    private fun setShopStatusType(shopStatusUiModel: ShopStatusUiModel) {
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
        shopType?.let { itemView.shopStatusHeader?.setImageDrawable(ContextCompat.getDrawable(context, it.shopTypeHeaderRes)) }
        itemView.shopStatusHeaderIcon?.run {
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
    }

}