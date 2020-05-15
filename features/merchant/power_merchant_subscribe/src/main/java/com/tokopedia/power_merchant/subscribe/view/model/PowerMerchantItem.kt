package com.tokopedia.power_merchant.subscribe.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PowerMerchantItem(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val icon: Int
)