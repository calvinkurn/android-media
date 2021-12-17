package com.tokopedia.seller.menu.common.view.uimodel.base

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.tokopedia.seller.menu.common.R

sealed class ShopType(@LayoutRes val shopTypeLayoutRes: Int,
                      @DrawableRes val shopTypeHeaderRes: Int,
                      @DrawableRes val shopTypeHeaderIconRes: Int? = null
) {

    companion object {
        val REGULAR_MERCHANT_LAYOUT = R.layout.setting_shop_status_regular
        val POWER_MERCHANT_LAYOUT = R.layout.setting_shop_status_pm
        val OFFICIAL_STORE_LAYOUT = R.layout.setting_shop_status_os
        val POWER_MERCHANT_PRO_LAYOUT = R.layout.setting_shop_status_pm_pro

        val REGULAR_MERCHANT_HEADER = R.drawable.setting_regular_header
        val POWER_MERCHANT_HEADER = R.drawable.setting_pm_header_background
        val OFFICIAL_STORE_HEADER = R.drawable.setting_os_header_background

        val POWER_MERCHANT_HEADER_ICON = R.drawable.ic_pm_icon_header
        val POWER_MERCHANT_PRO_HEADER_ICON = R.drawable.ic_pmpro_icon_header
        val OFFICIAL_STORE_HEADER_ICON = R.drawable.ic_os_icon_header
    }
    object OfficialStore : ShopType(OFFICIAL_STORE_LAYOUT, OFFICIAL_STORE_HEADER, OFFICIAL_STORE_HEADER_ICON)
}

sealed class PowerMerchantStatus : ShopType(POWER_MERCHANT_LAYOUT, POWER_MERCHANT_HEADER, POWER_MERCHANT_HEADER_ICON) {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
}

sealed class RegularMerchant : ShopType(REGULAR_MERCHANT_LAYOUT, REGULAR_MERCHANT_HEADER) {
    object NeedUpgrade: RegularMerchant()
}

sealed class PowerMerchantProStatus : ShopType(POWER_MERCHANT_PRO_LAYOUT, POWER_MERCHANT_HEADER, POWER_MERCHANT_PRO_HEADER_ICON) {
    object Advanced: PowerMerchantProStatus()
    object Expert: PowerMerchantProStatus()
    object Ultimate: PowerMerchantProStatus()
    object InActive: PowerMerchantProStatus()
}
