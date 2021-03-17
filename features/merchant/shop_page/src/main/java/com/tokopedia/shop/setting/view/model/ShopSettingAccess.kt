package com.tokopedia.shop.setting.view.model

data class ShopSettingAccess(
        val isAddressAccessAuthorized: Boolean = true,
        val isEtalaseAccessAuthorized: Boolean = true,
        val isInfoAccessAuthorized: Boolean = true,
        val isNotesAccessAuthorized: Boolean = true,
        val isShipmentAccessAuthorized: Boolean = true,
        val isProductManageAccessAuthorized: Boolean = true
)