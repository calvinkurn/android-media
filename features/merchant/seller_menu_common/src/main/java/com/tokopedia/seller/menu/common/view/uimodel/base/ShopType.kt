package com.tokopedia.seller.menu.common.view.uimodel.base

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.tokopedia.seller.menu.common.R

sealed class ShopType(@LayoutRes val shopTypeLayoutRes: Int,
                      @DrawableRes val shopTypeHeaderRes: Int,
                      val shopTypeHeaderIconUrl: String
) {

    companion object {
        val REGULAR_MERCHANT_LAYOUT = R.layout.setting_shop_status_regular
        val POWER_MERCHANT_LAYOUT = R.layout.setting_shop_status_pm
        val OFFICIAL_STORE_LAYOUT = R.layout.setting_shop_status_os
        val POWER_MERCHANT_PRO_LAYOUT = R.layout.setting_shop_status_pm_pro

        val REGULAR_MERCHANT_HEADER = R.drawable.setting_rm_header_background
        val POWER_MERCHANT_HEADER = R.drawable.setting_pm_header_background
        val OFFICIAL_STORE_HEADER = R.drawable.setting_os_header_background

        const val REGULAR_MERCHANT_ILLUSTRATION_URL = "https://images.tokopedia.net/img/android/seller_home/tokopedia_seller_anniv_banner_illu_rm_2x.png"
        const val POWER_MERCHANT_ILLUSTRATION_URL = "https://images.tokopedia.net/img/android/seller_home/tokopedia_seller_anniv_banner_illu_pm_2x.png"
        const val POWER_MERCHANT_PRO_MERCHANT_ILLUSTRATION_URL = "https://images.tokopedia.net/img/android/seller_home/tokopedia_seller_anniv_banner_illu_pm_pro_2x.png"
        const val OFFICIAL_STORE_MERCHANT_ILLUSTRATION_URL = "https://images.tokopedia.net/img/android/seller_home/tokopedia_seller_anniv_banner_illu_os_2x.png"
    }
    object OfficialStore : ShopType(OFFICIAL_STORE_LAYOUT, OFFICIAL_STORE_HEADER, OFFICIAL_STORE_MERCHANT_ILLUSTRATION_URL)
}

sealed class PowerMerchantStatus : ShopType(POWER_MERCHANT_LAYOUT, POWER_MERCHANT_HEADER, POWER_MERCHANT_ILLUSTRATION_URL) {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
}

sealed class RegularMerchant : ShopType(REGULAR_MERCHANT_LAYOUT, REGULAR_MERCHANT_HEADER, REGULAR_MERCHANT_ILLUSTRATION_URL) {
    object NeedUpgrade: RegularMerchant()
}

sealed class PowerMerchantProStatus : ShopType(POWER_MERCHANT_PRO_LAYOUT, POWER_MERCHANT_HEADER, POWER_MERCHANT_PRO_MERCHANT_ILLUSTRATION_URL) {
    object Advanced: PowerMerchantProStatus()
    object Expert: PowerMerchantProStatus()
    object Ultimate: PowerMerchantProStatus()
    object InActive: PowerMerchantProStatus()
}
