package com.tokopedia.vouchercreation.product.create.view.uimodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CouponTargetUiModel(
    @DrawableRes val iconRes: Int,
    @StringRes val titleStringRes: Int,
    @StringRes val descriptionStringRes: Int,
    val value: CouponTargetEnum,
    var selected: Boolean
)

enum class CouponTargetEnum(val value: Int){
    PRIVATE(0),
    PUBLIC(1)
}
