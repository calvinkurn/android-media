package com.tokopedia.sellerhome.settings.view.uimodel.base

sealed class ShopType {
    object OfficialStore : ShopType()
}

sealed class PowerMerchantStatus : ShopType() {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
    object OnVerification: PowerMerchantStatus()
}

sealed class RegularMerchant : ShopType() {
    object NeedUpdate: RegularMerchant()
    object OnVerification: RegularMerchant()
}