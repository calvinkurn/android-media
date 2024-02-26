package com.tokopedia.shopdiscount.subsidy.model.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopDiscountProductRuleUiModel(
    val isAbleToOptOut: Boolean = false
) : Parcelable
