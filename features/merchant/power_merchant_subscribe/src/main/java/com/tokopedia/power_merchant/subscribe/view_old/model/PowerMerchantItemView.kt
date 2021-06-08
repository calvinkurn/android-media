package com.tokopedia.power_merchant.subscribe.view_old.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class PowerMerchantItemView(
    @StringRes open val title: Int,
    @StringRes open val description: Int,
    @DrawableRes open val icon: Int,
    open val clickableText: Int? = null,
    open val clickableUrl: String? = null,
    open val boldTextList: List<Int>? = null
) {
    data class PowerMerchantFeature(
        override val title: Int,
        override val description: Int,
        override val icon: Int,
        override val clickableText: Int? = null,
        override val clickableUrl: String? = null,
        override val boldTextList: List<Int>? = null
    ): PowerMerchantItemView(title, description, icon, clickableText, clickableUrl)

    data class PowerMerchantBenefit(
        override val title: Int,
        override val description: Int,
        override val icon: Int,
        override val clickableText: Int? = null,
        override val clickableUrl: String? = null
    ): PowerMerchantItemView(title, description, icon, clickableText, clickableUrl)
}