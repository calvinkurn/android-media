package com.tokopedia.shop.settings.setting.data

data class ShopSettingAccess(
        val isAddressAccessAuthorized: Boolean = true,
        val isEtalaseAccessAuthorized: Boolean = true,
        val isInfoAccessAuthorized: Boolean = true,
        val isNotesAccessAuthorized: Boolean = true,
        val isShipmentAccessAuthorized: Boolean = true,
        val isProductManageAccessAuthorized: Boolean = true,
        val isContentManageAccessAuthorized: Boolean = true,
)
