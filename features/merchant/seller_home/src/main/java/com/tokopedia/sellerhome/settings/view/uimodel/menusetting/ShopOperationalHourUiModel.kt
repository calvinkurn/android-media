package com.tokopedia.sellerhome.settings.view.uimodel.menusetting

import androidx.annotation.StringRes

data class ShopOperationalHourUiModel(
    @StringRes val status: Int,
    val labelType: Int,
    val startTime: String,
    val endTime: String
)