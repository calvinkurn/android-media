package com.tokopedia.sellerhome.settings.view.uimodel.menusetting

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ShopOperationalUiModel(
    val time: String,
    @StringRes val timeLabel: Int?,
    @StringRes val status: Int,
    @DrawableRes val icon: Int,
    val labelType: Int,
    val hasShopSettingsAccess: Boolean
)