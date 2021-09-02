package com.tokopedia.sellerhome.settings.view.adapter.uimodel

import androidx.annotation.ColorRes

data class ShopOperationalData(
    val isShopOpen: Boolean,
    val isShopClosed: Boolean,
    val operationalIconType: Int,
    val operationalIconColor: Int,
    val timeDescription: String
)