package com.tokopedia.sellerhome.settings.view.uimodel.menusetting

data class MenuSettingAccess(
        val isAddressAccessAuthorized: Boolean = true,
        val isInfoAccessAuthorized: Boolean = true,
        val isNotesAccessAuthorized: Boolean = true,
        val isShipmentAccessAuthorized: Boolean = true
)