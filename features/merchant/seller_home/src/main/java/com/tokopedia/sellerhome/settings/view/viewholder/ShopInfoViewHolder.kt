package com.tokopedia.sellerhome.settings.view.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import kotlinx.android.synthetic.main.fragment_other_setting.view.*
import kotlinx.android.synthetic.main.setting_shop_info_layout.view.*
import kotlinx.android.synthetic.main.setting_shop_status_pm.view.*
import kotlinx.android.synthetic.main.setting_shop_status_regular.view.*

class ShopInfoViewHolder(private val itemView: View,
                         private val context: Context) {

    companion object {
        private val REGULAR_MERCHANT_LAYOUT = R.layout.setting_shop_status_regular
        private val POWER_MERCHANT_LAYOUT = R.layout.setting_shop_status_pm
        private val OFFICIAL_STORE_LAYOUT = R.layout.setting_shop_status_os
        private val SHIMMER_STATUS_LAYOUT = R.layout.setting_shop_status_shimmer
        private val GREEN_TIP = R.drawable.setting_pm_green_tip
        private val GREEN_TEXT_COLOR = R.color.setting_shop_status_green
        private val GREY_TIP = R.drawable.setting_pm_grey_tip
        private val GREY_TEXT_COLOR = R.color.setting_grey_text
        private val RED_TEXT_COLOR = R.color.setting_red_text

        private const val FOLLOWERS = "Followers"
        private const val UPDATE = "Update"
        private const val VERIFIKASI = "Verifikasi"
        private const val AKTIF = "Aktif"
        private const val TIDAK_AKTIF = "Tidak Aktif"
        private const val SEDANG_DIVERIFIKASI = "Sedang Diverifikasi"
    }

    fun onSuccessGetShopGeneralInfoData(uiModel: GeneralShopInfoUiModel) {
        uiModel.run {
            setShopName(shopName)
            setShopAvatar(shopAvatarUrl)
            setShopStatusType(shopType)
        }
        removeGeneralInfoDataShimmer()
    }

    fun onLoadingGetShopGeneralInfoData() {
        itemView.run {
            shopInfoLayout.shopName.text = null
            shopInfoLayout.shopImage.urlSrc = ""
            showGeneralInfoDataShimmer()
        }
    }

    private fun setShopName(shopName: String) {
        itemView.shopInfoLayout.shopName.text = shopName
    }

    private fun setShopAvatar(shopAvatarUrl: String) {
        itemView.shopInfoLayout.shopImage.urlSrc = shopAvatarUrl
    }

    private fun setShopStatusType(shopType: ShopType) {
        val shopStatusLayout: View = when(shopType) {
            is RegularMerchant -> {
                val layoutInflater = LayoutInflater.from(context).inflate(REGULAR_MERCHANT_LAYOUT, null, false)
                layoutInflater.setRegularMerchantShopStatus(shopType)
            }
            is PowerMerchantStatus -> {
                val layoutInflater = LayoutInflater.from(context).inflate(POWER_MERCHANT_LAYOUT, null, false)
                layoutInflater.setPowerMerchantShopStatus(shopType)
            }
            is ShopType.OfficialStore -> LayoutInflater.from(context).inflate(OFFICIAL_STORE_LAYOUT, null, false)
        }
        (itemView.shopStatus as LinearLayout).run {
            removeAllViews()
            addView(shopStatusLayout)
        }
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
        when(powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                statusText = AKTIF
                textColor = GREEN_TEXT_COLOR
                statusDrawable = GREEN_TIP }
            is PowerMerchantStatus.NotActive -> {
                statusText = TIDAK_AKTIF
                textColor = RED_TEXT_COLOR }
            is PowerMerchantStatus.OnVerification -> { }
        }
        powerMerchantStatusText.text = statusText
        powerMerchantStatusText.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
        powerMerchantLeftStatus.background = ResourcesCompat.getDrawable(resources, statusDrawable, null)
        return this
    }

    private fun View.showGeneralInfoDataShimmer() {
        shopInfoLayout.shimmerShopName.visibility = View.VISIBLE
        val statusShimmerLayout = LayoutInflater.from(context).inflate(SHIMMER_STATUS_LAYOUT, null, false)
        (shopStatus as LinearLayout).run {
            removeAllViews()
            addView(statusShimmerLayout)
        }
    }

    private fun removeGeneralInfoDataShimmer() {
        itemView.shopInfoLayout.shimmerShopName.visibility = View.GONE
    }

    interface Listener {

    }

}