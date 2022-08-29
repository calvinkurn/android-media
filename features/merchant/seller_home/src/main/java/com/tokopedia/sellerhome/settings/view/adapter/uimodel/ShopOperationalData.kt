package com.tokopedia.sellerhome.settings.view.adapter.uimodel

import androidx.annotation.StringRes

data class ShopOperationalData(
    val isShopOpen: Boolean,
    val isShopClosed: Boolean,
    val isWeeklyOperationalClosed: Boolean,
    val isShopActive: Boolean,
    @StringRes val timeDescriptionRes: Int? = null,
    val timeDescription: String? = null,
    val shopSettingAccess: Boolean
)