package com.tokopedia.sellerhome.settings.view.adapter.uimodel

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class ShopOperationalData(
    val isShopOpen: Boolean,
    val isShopClosed: Boolean,
    val operationalIconType: Int,
    @ColorRes val operationalIconColorRes: Int,
    @StringRes val timeDescriptionRes: Int? = null,
    val timeDescription: String? = null,
    val shopSettingAccess: Boolean
)