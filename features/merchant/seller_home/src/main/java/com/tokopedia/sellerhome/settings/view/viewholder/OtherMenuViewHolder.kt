package com.tokopedia.sellerhome.settings.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.sellerhome.settings.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.sellerhome.settings.analytics.sendTrackingManual
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.BalanceUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.ShopAvatarUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.ShopStatusUiModel
import kotlinx.android.synthetic.main.fragment_other_menu.view.*
import kotlinx.android.synthetic.main.fragment_other_menu.view.shopInfoLayout
import kotlinx.android.synthetic.main.setting_balance.view.*
import kotlinx.android.synthetic.main.setting_partial_main_info_success.view.*
import kotlinx.android.synthetic.main.setting_partial_shop_info_error.view.*
import kotlinx.android.synthetic.main.setting_partial_shop_info_success.view.*
import kotlinx.android.synthetic.main.setting_shop_status_pm.view.*
import kotlinx.android.synthetic.main.setting_shop_status_regular.view.*

class OtherMenuViewHolder(private val itemView: View,
                          private val context: Context,
                          private val listener: Listener) {

    companion object {
        private val GREEN_TIP = R.drawable.setting_tip_bar_enabled
        private val GREEN_TEXT_COLOR = R.color.setting_green
        private val GREY_TIP = R.drawable.setting_tip_bar_disabled
        private val GREY_TEXT_COLOR = R.color.setting_grey_text
        private val RED_TEXT_COLOR = R.color.setting_red_text
        private val GREY_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant_inactive
        private val GREEN_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant

    }

    fun onSuccessGetSettingShopInfoData(uiModel: SettingShopInfoUiModel) {
        val successLayout = LayoutInflater.from(context).inflate(R.layout.setting_partial_shop_info_success, null, false)
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(successLayout)
            setOnClickAction()
        }
        uiModel.run {
            setShopName(shopName)
            setShopAvatar(shopAvatarUiModel)
            setShopStatusType(shopStatusUiModel)
            setSaldoBalance(saldoBalanceUiModel)
            setKreditTopadsBalance(topadsBalanceUiModel)
            onSuccessGetShopBadge(shopBadges)
            onSuccessGetTotalFollowers(shopFollowers)
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
        errorLayout?.settingLocalLoad?.run {
            title?.text = context.resources.getString(R.string.setting_error_message)
            description?.text = context.resources.getString(R.string.setting_error_description)
            refreshBtn?.setOnClickListener {
                listener.onRefreshShopInfo()
            }
        }
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(errorLayout)
        }
    }

    private fun onSuccessGetShopBadge(shopBadgeUrl: String) {
        itemView.shopInfoLayout.shopBadges?.let {
            ImageHandler.LoadImage(it, shopBadgeUrl)
        }
    }

    @SuppressLint("SetTextI18n")
    fun onSuccessGetTotalFollowers(totalFollowing: Int) {
        itemView.shopInfoLayout.shopFollowers?.text = "$totalFollowing ${context.resources.getString(R.string.setting_followers)}"
    }

    private fun setShopName(shopName: String) {
        itemView.run {
            shopInfoLayout.shopName?.run {
                text = shopName
                setOnClickListener {
                    listener.onShopInfoClicked()
                    sendTrackingManual(
                            eventName = SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
                            eventCategory = SettingTrackingConstant.OTHERS_TAB,
                            eventAction = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_NAME}",
                            eventLabel = ""
                    )
                }
            }
        }
    }

    private fun setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        itemView.shopInfoLayout.shopImage?.run {
            urlSrc = shopAvatarUiModel.shopAvatarUrl
            sendSettingShopInfoImpressionTracking(shopAvatarUiModel, context)
            setOnClickListener {
                listener.onShopInfoClicked()
                shopAvatarUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setSaldoBalance(saldoBalanceUiModel: BalanceUiModel) {
        itemView.saldoBalance.run {
            balanceTitle?.text = context.resources.getString(R.string.setting_balance)
            balanceValue?.text = saldoBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(saldoBalanceUiModel, context)
            saldoBalance.setOnClickListener {
                listener.onSaldoClicked()
                saldoBalanceUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setKreditTopadsBalance(topadsBalanceUiModel: BalanceUiModel) {
        itemView.topAdsBalance.run {
            balanceTitle?.text = context.resources.getString(R.string.setting_topads_credits)
            balanceValue?.text = topadsBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(topadsBalanceUiModel, context)
            topAdsBalance.setOnClickListener {
                listener.onKreditTopadsClicked()
                topadsBalanceUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setShopStatusType(shopStatusUiModel: ShopStatusUiModel) {
        val shopType = shopStatusUiModel.shopType
        showShopStatusHeader(shopType)
        val layoutInflater = LayoutInflater.from(context).inflate(shopType.shopTypeLayoutRes, null, false)
        val shopStatusLayout: View = when(shopType) {
            is RegularMerchant -> {
                listener.onStatusBarNeedDarkColor(true)
                layoutInflater.apply {
                    setRegularMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, context)
                    rightRectangle.setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is PowerMerchantStatus -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater.apply {
                    setPowerMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, context)
                    setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is ShopType.OfficialStore -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater.apply {
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, context)
                }
            }
        }
        (itemView.shopStatus as LinearLayout).run {
            removeAllViews()
            addView(shopStatusLayout)
        }
    }

    private fun showShopStatusHeader(shopType: ShopType) {
        itemView.shopStatusHeader?.background = ContextCompat.getDrawable(context, shopType.shopTypeHeaderRes)
    }

    private fun View.setRegularMerchantShopStatus(regularMerchant: RegularMerchant) : View {
        regularMerchantStatus.run {
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
                powerMerchantText?.text = context.resources.getString(R.string.regular_merchant)
            }
        }
        powerMerchantStatusText?.text = statusText
        powerMerchantStatusText?.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
        powerMerchantLeftStatus?.background = ResourcesCompat.getDrawable(resources, statusDrawable, null)
        powerMerchantIcon?.setImageDrawable(ResourcesCompat.getDrawable(resources, powerMerchantDrawableIcon, null))
        return this
    }

    private fun View.setOnClickAction() {
        shopInfoLayout?.run {
            settingShopNext.setOnClickListener {
                listener.onShopInfoClicked()
                sendTrackingManual(
                        eventName = SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
                        eventCategory = SettingTrackingConstant.OTHERS_TAB,
                        eventAction = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_ARROW}",
                        eventLabel = ""
                )
            }
            shopFollowers.setOnClickListener { listener.onFollowersCountClicked() }
        }
    }

    interface Listener {
        fun onShopInfoClicked()
        fun onFollowersCountClicked()
        fun onSaldoClicked()
        fun onKreditTopadsClicked()
        fun onRefreshShopInfo()
        fun onStatusBarNeedDarkColor(isDefaultDark: Boolean)
    }

}