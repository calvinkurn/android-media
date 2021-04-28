package com.tokopedia.seller.menu.common.view.uimodel.base

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.tokopedia.seller.menu.common.R


sealed class ShopType(@LayoutRes val shopTypeLayoutRes: Int,
                      @DrawableRes val shopTypeHeaderRes: Int,
                      @DrawableRes val shopTypeHeaderIconRes: Int? = null) {

    companion object {
        val REGULAR_MERCHANT_LAYOUT = R.layout.setting_shop_status_regular
        val POWER_MERCHANT_LAYOUT = R.layout.setting_shop_status_pm
        val OFFICIAL_STORE_LAYOUT = R.layout.setting_shop_status_os

        val REGULAR_MERCHANT_HEADER = R.drawable.setting_rm_header_background_ramadhan
        val POWER_MERCHANT_HEADER = R.drawable.setting_pm_header_background_ramadhan
        val OFFICIAL_STORE_HEADER = R.drawable.setting_os_header_background_ramadhan
    }
    object OfficialStore : ShopType(OFFICIAL_STORE_LAYOUT, OFFICIAL_STORE_HEADER)
}

sealed class PowerMerchantStatus : ShopType(POWER_MERCHANT_LAYOUT, POWER_MERCHANT_HEADER) {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
    object OnVerification: PowerMerchantStatus()
}

sealed class RegularMerchant : ShopType(REGULAR_MERCHANT_LAYOUT, REGULAR_MERCHANT_HEADER) {
    object NeedUpgrade: RegularMerchant()
    object NeedVerification: RegularMerchant()
}