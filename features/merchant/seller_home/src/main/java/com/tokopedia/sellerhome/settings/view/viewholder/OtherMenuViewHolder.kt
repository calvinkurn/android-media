package com.tokopedia.sellerhome.settings.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller.menu.common.analytics.*
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_other_menu.view.*

class OtherMenuViewHolder(private val itemView: View,
                          private val context: Context,
                          private val listener: Listener,
                          private val trackingListener: SettingTrackingListener,
                          private val freeShippingTracker: SettingFreeShippingTracker,
                          private val userSession: UserSessionInterface) {

    companion object {
        private val GREEN_TIP = R.drawable.setting_tip_bar_enabled
        private val GREEN_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_G500
        private val GREY_TIP = R.drawable.setting_tip_bar_disabled
        private val GREY_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        private val RED_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_R500
        private val GREY_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant_inactive
        private val GREEN_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant
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
                        findViewById<LinearLayout>(R.id.saldoBalance)?.visible()
                        findViewById<LinearLayout>(R.id.topAdsBalance)?.visible()
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
                        findViewById<LinearLayout>(R.id.saldoBalance)?.gone()
                        findViewById<LinearLayout>(R.id.topAdsBalance)?.gone()
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
                        findViewById<LinearLayout>(R.id.saldoBalance)?.visible()
                        findViewById<LinearLayout>(R.id.topAdsBalance)?.visible()
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
            setOnClickAction()
        }
        setShopName(userSession.shopName)
        setShopAvatar(ShopAvatarUiModel(userSession.shopAvatar))
    }

    private fun setShopBadge(shopBadgeUiModel: ShopBadgeUiModel) {
        itemView.shopInfoLayout.findViewById<AppCompatImageView>(R.id.shopBadges)?.run {
            ImageHandler.LoadImage(this, shopBadgeUiModel.shopBadgeUrl)
            setOnClickListener {
                listener.onShopBadgeClicked()
                shopBadgeUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
        val shouldShowFollowers = shopTotalFollowersUiModel.shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val followersVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        itemView.shopInfoLayout.findViewById<Typography>(R.id.shopFollowers)?.run {
            visibility = followersVisibility
            text = "${shopTotalFollowersUiModel.shopFollowers} ${context.resources.getString(R.string.setting_followers)}"
            setOnClickListener {
                shopTotalFollowersUiModel.sendSettingShopInfoClickTracking()
                listener.onFollowersCountClicked()
            }
        }
    }

    fun setupFreeShippingLayout() {
        itemView.shopInfoLayout.findViewById<FrameLayout>(R.id.freeShippingLayout)?.apply {
            setOnClickListener {
                listener.onFreeShippingClicked()
                freeShippingTracker.trackFreeShippingClick()
            }
            visibility = View.VISIBLE

            freeShippingTracker.trackFreeShippingImpression()
        }
    }

    fun hideFreeShippingLayout() {
        itemView.shopInfoLayout.findViewById<FrameLayout>(R.id.freeShippingLayout)?.hide()
    }

    private fun setDotVisibility(shopFollowers: Long) {
        val shouldShowFollowers = shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val dotVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        val tvDot = itemView.shopInfoLayout.findViewById<Typography>(R.id.dot)
        tvDot?.visibility = dotVisibility
    }

    private fun setupSuccessLayout() {
        val successLayout = LayoutInflater.from(context).inflate(R.layout.setting_partial_shop_info_success, null, false)
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(successLayout)
            setOnClickAction()
        }
        setShopName(userSession.shopName)
        setShopAvatar(ShopAvatarUiModel(userSession.shopAvatar))
    }

    private fun setShopName(shopName: String) {
        itemView.run {
            shopInfoLayout.findViewById<Typography>(R.id.shopName)?.run {
                text = MethodChecker.fromHtml(shopName)
                setOnClickListener {
                    listener.onShopInfoClicked()
                    sendClickShopNameTracking()
                }
            }
        }
    }

    private fun setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        itemView.shopInfoLayout.findViewById<ImageUnify>(R.id.shopImage)?.run {
            urlSrc = shopAvatarUiModel.shopAvatarUrl
            sendSettingShopInfoImpressionTracking(shopAvatarUiModel, trackingListener::sendImpressionDataIris)
            setOnClickListener {
                listener.onShopInfoClicked()
                shopAvatarUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setSaldoBalance(saldoBalanceUiModel: BalanceUiModel) {
        itemView.findViewById<LinearLayout>(R.id.saldoBalance).run {
            findViewById<Typography>(R.id.balanceTitle)?.text = context.resources.getString(R.string.setting_balance)
            findViewById<Typography>(R.id.balanceValue)?.text = saldoBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(saldoBalanceUiModel, trackingListener::sendImpressionDataIris)
            findViewById<Typography>(R.id.balanceValue)?.setOnClickListener {
                listener.onSaldoClicked()
                saldoBalanceUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setKreditTopadsBalance(topadsBalanceUiModel: TopadsBalanceUiModel) {
        itemView.findViewById<LinearLayout>(R.id.topAdsBalance).run {
            findViewById<Typography>(R.id.topadsBalanceTitle)?.text = context.resources.getString(R.string.setting_topads_credits)
            findViewById<Typography>(R.id.topadsBalanceValue)?.text = topadsBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(topadsBalanceUiModel, trackingListener::sendImpressionDataIris)
            findViewById<Typography>(R.id.topadsBalanceValue)?.setOnClickListener {
                listener.onKreditTopadsClicked()
                topadsBalanceUiModel.sendSettingShopInfoClickTracking()
            }
            val isTopAdsUser = topadsBalanceUiModel.isTopAdsUser
            val topAdsTooltipDrawable =
                    if (isTopAdsUser) {
                        ContextCompat.getDrawable(context, R.drawable.ic_topads_active)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_topads_inactive)
                    }
            findViewById<AppCompatImageView>(R.id.topAdsStatusTooltip)?.run {
                setImageDrawable(topAdsTooltipDrawable)
                setOnClickListener {
                    listener.onTopAdsTooltipClicked(isTopAdsUser)
                }
            }
        }
    }

    private fun setShopStatusType(shopStatusUiModel: ShopStatusUiModel) {
        val shopType = shopStatusUiModel.shopType
        showShopStatusHeader(shopType, shopStatusUiModel.thematicIllustrationUrl)
        val layoutInflater = LayoutInflater.from(context).inflate(shopType.shopTypeLayoutRes, null, false)
        val shopStatusLayout: View = when(shopType) {
            is RegularMerchant -> {
                listener.onStatusBarNeedDarkColor(true)
                layoutInflater.apply {
                    setRegularMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    findViewById<AppCompatImageView>(R.id.rightRectangle).setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is PowerMerchantStatus -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater.apply {
                    setPowerMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is ShopType.OfficialStore -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater.apply {
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                }
            }
        }
        (itemView.findViewById(R.id.shopStatus) as LinearLayout).run {
            removeAllViews()
            addView(shopStatusLayout)
        }
    }

    private fun showShopStatusHeader(shopType: ShopType, thematicIllustrationUrl: String = "") {
        itemView.shopStatusHeader?.setImageResource(shopType.shopTypeHeaderRes)

        itemView.findViewById<AppCompatImageView>(R.id.iv_other_menu_thematic)?.let { thematicIv ->
            ImageHandler.loadImageWithoutPlaceholderAndError(thematicIv, thematicIllustrationUrl)
        }
    }

    private fun View.setRegularMerchantShopStatus(regularMerchant: RegularMerchant) : View {
        findViewById<Typography>(R.id.regularMerchantStatus).run {
            text = when(regularMerchant) {
                is RegularMerchant.NeedUpgrade -> context.resources.getString(R.string.setting_upgrade)
                is RegularMerchant.NeedVerification -> context.resources.getString(R.string.setting_verifikasi)
            }
        }

        return this
    }

    private fun View.setPowerMerchantShopStatus(powerMerchantStatus: PowerMerchantStatus) : View {
        var statusText = context.resources.getString(R.string.setting_on_verification)
        var textColor = GREY_TEXT_COLOR
        var statusDrawable = GREY_TIP
        var powerMerchantDrawableIcon = GREY_POWER_MERCHANT_ICON
        when(powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                statusText = context.resources.getString(R.string.setting_active)
                textColor = GREEN_TEXT_COLOR
                powerMerchantDrawableIcon = GREEN_POWER_MERCHANT_ICON
                statusDrawable = GREEN_TIP }
            is PowerMerchantStatus.NotActive -> {
                statusText = context.resources.getString(R.string.setting_not_active)
                textColor = RED_TEXT_COLOR }
            is PowerMerchantStatus.OnVerification -> {
                findViewById<Typography>(R.id.powerMerchantText)?.text = context.resources.getString(R.string.regular_merchant)
            }
        }
        findViewById<Typography>(R.id.powerMerchantStatusText)?.text = statusText
        findViewById<Typography>(R.id.powerMerchantStatusText)?.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById<AppCompatImageView>(R.id.powerMerchantLeftStatus)?.background = ResourcesCompat.getDrawable(resources, statusDrawable, null)
        } else {
            findViewById<AppCompatImageView>(R.id.powerMerchantLeftStatus)?.let {
                (it as? ImageView)?.setImageDrawable(ResourcesCompat.getDrawable(resources, statusDrawable, null))
            }
        }
        findViewById<AppCompatImageView>(R.id.powerMerchantIcon)?.setImageDrawable(ResourcesCompat.getDrawable(resources, powerMerchantDrawableIcon, null))
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
    }

}