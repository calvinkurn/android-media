package com.tokopedia.sellerhome.settings.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import kotlinx.android.synthetic.main.fragment_other_menu.view.*
import kotlinx.android.synthetic.main.setting_balance.view.*
import kotlinx.android.synthetic.main.setting_shop_info_layout.view.*
import kotlinx.android.synthetic.main.setting_shop_status_pm.view.*
import kotlinx.android.synthetic.main.setting_shop_status_regular.view.*

class OtherMenuViewHolder(private val itemView: View,
                          private val context: Context) {

    companion object {
        private val SHIMMER_STATUS_LAYOUT = R.layout.setting_shop_status_shimmer
        private val GREEN_TIP = R.drawable.setting_pm_green_tip
        private val GREEN_TEXT_COLOR = R.color.setting_shop_status_green
        private val GREY_TIP = R.drawable.setting_pm_grey_tip
        private val GREY_TEXT_COLOR = R.color.setting_grey_text
        private val RED_TEXT_COLOR = R.color.setting_red_text
        private val GREY_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant_inactive
        private val GREEN_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant

        private const val FOLLOWERS = "Followers"
        private const val UPDATE = "Update"
        private const val VERIFIKASI = "Verifikasi"
        private const val AKTIF = "Aktif"
        private const val TIDAK_AKTIF = "Tidak Aktif"
        private const val SEDANG_DIVERIFIKASI = "Sedang Diverifikasi"
        private const val SALDO = "Saldo"
        private const val KREDIT_TOPADS = "Kredit TopAds"
    }

    fun initBindView() {
        itemView.saldoBalance.balanceTitle.text = SALDO
        itemView.topAdsBalance.balanceTitle.text = KREDIT_TOPADS
    }

    fun onSuccessGetShopGeneralInfoData(uiModel: GeneralShopInfoUiModel) {
        uiModel.run {
            setShopName(shopName)
            setShopAvatar(shopAvatarUrl)
            setShopStatusType(shopType)
            setSaldoBalance(saldoBalance)
            setKreditTopadsBalance(kreditTopAdsBalance)
        }
        itemView.removeGeneralInfoDataShimmer()
    }

    fun onSuccessGetShopBadge(shopBadgeUrl: String) {
        itemView.shopInfoLayout.shopBadges.urlSrc = shopBadgeUrl
    }

    @SuppressLint("SetTextI18n")
    fun onSuccessGetTotalFollowing(totalFollowing: Int) {
        itemView.shopInfoLayout.shopFollowing.text = "$totalFollowing $FOLLOWERS"
        itemView.shimmerFollowing.visibility = View.GONE
    }

    fun onLoadingGetShopGeneralInfoData() {
        itemView.run {
            shopInfoLayout.shopName.text = null
            shopInfoLayout.shopImage.urlSrc = ""
            saldoBalance.balanceValue.text = null
            topAdsBalance.balanceValue.text = null
            showGeneralInfoDataShimmer()
        }
    }

    fun onLoadingGetShopBadge() {
        itemView.shopInfoLayout.shopBadges.urlSrc = ""
    }

    fun onLoadingGetTotalFollowing() {
        itemView.shopInfoLayout.shopFollowing.text = ""
        itemView.shimmerFollowing.visibility = View.VISIBLE
    }

    private fun setShopName(shopName: String) {
        itemView.shopInfoLayout.shopName.text = shopName
    }

    private fun setShopAvatar(shopAvatarUrl: String) {
        itemView.shopInfoLayout.shopImage.urlSrc = shopAvatarUrl
    }

    private fun setSaldoBalance(balance: String) {
        itemView.saldoBalance.balanceValue.text = balance
    }

    private fun setKreditTopadsBalance(balance: String) {
        itemView.topAdsBalance.balanceValue.text = balance
    }

    private fun setShopStatusType(shopType: ShopType) {
        showShopStatusHeader(shopType)
        val layoutInflater = LayoutInflater.from(context).inflate(shopType.shopTypeLayoutRes, null, false)
        val shopStatusLayout: View = when(shopType) {
            is RegularMerchant -> {
                layoutInflater.setRegularMerchantShopStatus(shopType)
            }
            is PowerMerchantStatus -> {
                layoutInflater.setPowerMerchantShopStatus(shopType)
            }
            is ShopType.OfficialStore -> layoutInflater
        }
        (itemView.shopStatus as LinearLayout).run {
            removeAllViews()
            addView(shopStatusLayout)
        }
    }

    private fun showShopStatusHeader(shopType: ShopType) {
        itemView.shopStatusHeader.background = ContextCompat.getDrawable(context, shopType.shopTypeHeaderRes)
    }

    private fun View.setRegularMerchantShopStatus(regularMerchant: RegularMerchant) : View {
        regularMerchantStatus.text =
                when(regularMerchant) {
                    is RegularMerchant.NeedUpdate -> UPDATE
                    is RegularMerchant.OnVerification -> VERIFIKASI
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
            is PowerMerchantStatus.OnVerification -> { }
        }
        powerMerchantStatusText.text = statusText
        powerMerchantStatusText.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
        powerMerchantLeftStatus.background = ResourcesCompat.getDrawable(resources, statusDrawable, null)
        powerMerchantIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, powerMerchantDrawableIcon, null))
        return this
    }

    private fun View.showGeneralInfoDataShimmer() {
        shopInfoLayout.shimmerShopName.visibility = View.VISIBLE
        val statusShimmerLayout = LayoutInflater.from(context).inflate(SHIMMER_STATUS_LAYOUT, null, false)
        (shopStatus as LinearLayout).run {
            removeAllViews()
            addView(statusShimmerLayout)
        }
        saldoBalance.shimmeringBalanceValue.visibility = View.VISIBLE
        topAdsBalance.shimmeringBalanceValue.visibility = View.VISIBLE
    }

    private fun View.removeGeneralInfoDataShimmer() {
        shopInfoLayout.shimmerShopName.visibility = View.GONE
        saldoBalance.shimmeringBalanceValue.visibility = View.GONE
        topAdsBalance.shimmeringBalanceValue.visibility = View.GONE
    }

}