package com.tokopedia.sellerhome.settings.view.uimodel.base

sealed class ShopType {
    data class PowerMerchant(val powerMerchantStatus: PowerMerchantStatus) : ShopType()
    object RegularMerchant : ShopType()
    object OfficialStore : ShopType()
}

sealed class PowerMerchantStatus {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
    object OnVerification: PowerMerchantStatus()
}