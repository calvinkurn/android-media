package com.tokopedia.sellerhome.settings.view.uimodel.base

import com.tokopedia.sellerhome.R


sealed class ShopType(val shopTypeLayoutRes: Int,
                      val shopTypeHeaderRes: Int) {

    companion object {
        val REGULAR_MERCHANT_LAYOUT = R.layout.setting_shop_status_regular
        val POWER_MERCHANT_LAYOUT = R.layout.setting_shop_status_pm
        val OFFICIAL_STORE_LAYOUT = R.layout.setting_shop_status_os

        val REGULAR_MERCHANT_HEADER = R.drawable.setting_regular_header
        val POWER_MERCHANT_HEADER = R.drawable.setting_pm_header
        val OFFICIAL_STORE_HEADER = R.drawable.setting_os_header
    }
    object OfficialStore : ShopType(OFFICIAL_STORE_LAYOUT, OFFICIAL_STORE_HEADER)
}

sealed class PowerMerchantStatus : ShopType(POWER_MERCHANT_LAYOUT, POWER_MERCHANT_HEADER) {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
    object OnVerification: PowerMerchantStatus()
}

sealed class RegularMerchant : ShopType(REGULAR_MERCHANT_LAYOUT, REGULAR_MERCHANT_HEADER) {
    object NeedUpdate: RegularMerchant()
    object OnVerification: RegularMerchant()
}