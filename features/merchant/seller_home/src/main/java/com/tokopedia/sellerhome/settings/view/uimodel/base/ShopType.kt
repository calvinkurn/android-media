package com.tokopedia.sellerhome.settings.view.uimodel.base

import com.tokopedia.sellerhome.R


sealed class ShopType(val shopTypeLayoutRes: Int,
                      val shopTypeHeaderRes: Int,
                      val shopTypeHeaderIconRes: Int = 0) {

    companion object {
        val REGULAR_MERCHANT_LAYOUT = R.layout.setting_shop_status_regular
        val POWER_MERCHANT_LAYOUT = R.layout.setting_shop_status_pm
        val OFFICIAL_STORE_LAYOUT = R.layout.setting_shop_status_os

        val REGULAR_MERCHANT_HEADER = R.drawable.setting_regular_header
        val POWER_MERCHANT_HEADER = R.drawable.setting_pm_header_background
        val OFFICIAL_STORE_HEADER = R.drawable.setting_os_header_background

        val POWER_MERCHANT_HEADER_ICON = R.drawable.ic_icon_header_pm
        val OFFICIAL_STORE_HEADER_ICON = R.drawable.ic_icon_header_os
    }
    object OfficialStore : ShopType(OFFICIAL_STORE_LAYOUT, OFFICIAL_STORE_HEADER, OFFICIAL_STORE_HEADER_ICON)
}

sealed class PowerMerchantStatus : ShopType(POWER_MERCHANT_LAYOUT, POWER_MERCHANT_HEADER, POWER_MERCHANT_HEADER_ICON) {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
    object OnVerification: PowerMerchantStatus()
}

sealed class RegularMerchant : ShopType(REGULAR_MERCHANT_LAYOUT, REGULAR_MERCHANT_HEADER) {
    object NeedUpgrade: RegularMerchant()
    object NeedVerification: RegularMerchant()
}