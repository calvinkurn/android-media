package com.tokopedia.seller.menu.common.view.uimodel.base

sealed class ShopType {
    object OfficialStore : ShopType()
}

sealed class PowerMerchantStatus : ShopType() {
    object Active: PowerMerchantStatus()
    object NotActive: PowerMerchantStatus()
}

sealed class RegularMerchant : ShopType() {
    object NeedUpgrade: RegularMerchant()
    object Verified: RegularMerchant()
    object Pending: RegularMerchant()
}

sealed class PowerMerchantProStatus : ShopType() {
    object Advanced: PowerMerchantProStatus()
    object Expert: PowerMerchantProStatus()
    object Ultimate: PowerMerchantProStatus()
    object InActive: PowerMerchantProStatus()
}
