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
import com.tokopedia.sellerhome.settings.view.uimodel.SettingShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import kotlinx.android.synthetic.main.fragment_other_menu.view.*
import kotlinx.android.synthetic.main.fragment_other_menu.view.shopInfoLayout
import kotlinx.android.synthetic.main.setting_balance.view.*
import kotlinx.android.synthetic.main.setting_partial_shop_info_error.view.*
import kotlinx.android.synthetic.main.setting_partial_shop_info_success.view.*
import kotlinx.android.synthetic.main.setting_shop_info_layout.view.*
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

        private const val REGULAR_MERCHANT = "Regular Merchant"
        private const val FOLLOWERS = "Followers"
        private const val UPDATE = "Update"
        private const val VERIFIKASI = "Verifikasi"
        private const val AKTIF = "Aktif"
        private const val TIDAK_AKTIF = "Tidak Aktif"
        private const val SEDANG_DIVERIFIKASI = "Sedang Diverifikasi"
        private const val SALDO = "Saldo"
        private const val KREDIT_TOPADS = "Kredit TopAds"
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
            setShopAvatar(shopAvatar)
            setShopStatusType(shopType)
            setSaldoBalance(saldoBalance)
            setKreditTopadsBalance(kreditTopAdsBalance)
            onSuccessGetShopBadge(shopBadges)
            onSuccessGetTotalFollowers(shopFollowers)
        }
        itemView.shopInfoLayout.saldoBalance.balanceTitle.text = SALDO
        itemView.shopInfoLayout.topAdsBalance.balanceTitle.text = KREDIT_TOPADS
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
        itemView.shopInfoLayout.shopFollowers?.text = "$totalFollowing $FOLLOWERS"
    }

    private fun setShopName(shopName: String) {
        itemView.shopInfoLayout.shopName?.text = shopName
    }

    private fun setShopAvatar(shopAvatarUrl: String) {
        itemView.shopInfoLayout.shopImage?.urlSrc = shopAvatarUrl
    }

    private fun setSaldoBalance(balance: String) {
        itemView.saldoBalance.balanceValue?.text = balance
    }

    private fun setKreditTopadsBalance(balance: String) {
        itemView.topAdsBalance.balanceValue?.text = balance
    }

    private fun setShopStatusType(shopType: ShopType) {
        showShopStatusHeader(shopType)
        val layoutInflater = LayoutInflater.from(context).inflate(shopType.shopTypeLayoutRes, null, false)
        val shopStatusLayout: View = when(shopType) {
            is RegularMerchant -> {
                listener.onStatusBarNeedDarkColor(true)
                layoutInflater.setRegularMerchantShopStatus(shopType)
            }
            is PowerMerchantStatus -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater.setPowerMerchantShopStatus(shopType)
            }
            is ShopType.OfficialStore -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater
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
                is RegularMerchant.NeedUpdate -> UPDATE
                is RegularMerchant.OnVerification -> VERIFIKASI
            }
            setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
            }
        }
        return this
    }

    private fun View.setPowerMerchantShopStatus(powerMerchantStatus: PowerMerchantStatus) : View {
        var statusText = SEDANG_DIVERIFIKASI
        var textColor = GREY_TEXT_COLOR
        var statusDrawable = GREY_TIP
        var powerMerchantDrawableIcon = GREY_POWER_MERCHANT_ICON
        when(powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                statusText = AKTIF
                textColor = GREEN_TEXT_COLOR
                powerMerchantDrawableIcon = GREEN_POWER_MERCHANT_ICON
                statusDrawable = GREEN_TIP }
            is PowerMerchantStatus.NotActive -> {
                statusText = TIDAK_AKTIF
                textColor = RED_TEXT_COLOR }
            is PowerMerchantStatus.OnVerification -> {
                powerMerchantText?.text = REGULAR_MERCHANT
            }
        }
        powerMerchantStatusText?.text = statusText
        powerMerchantStatusText?.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
        powerMerchantLeftStatus?.background = ResourcesCompat.getDrawable(resources, statusDrawable, null)
        powerMerchantIcon?.setImageDrawable(ResourcesCompat.getDrawable(resources, powerMerchantDrawableIcon, null))
        setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
        }
        return this
    }

    private fun View.setOnClickAction() {
        shopInfoLayout?.run {
            shopImage.setOnClickListener { listener.onShopInfoClicked() }
            shopName.setOnClickListener { listener.onShopInfoClicked() }
            settingShopNext.setOnClickListener { listener.onShopInfoClicked() }
            shopFollowers.setOnClickListener { listener.onFollowersCountClicked() }
        }
        saldoBalance.setOnClickListener { listener.onSaldoClicked() }
        topAdsBalance.setOnClickListener { listener.onKreditTopadsClicked() }
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