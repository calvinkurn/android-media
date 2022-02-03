package com.tokopedia.vouchercreation.product.create.view.uimodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation

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

internal fun CouponTargetEnum?.convertToCouponInformationTarget() =
    if (this == CouponTargetEnum.PUBLIC) {
        CouponInformation.Target.PUBLIC
    } else {
        CouponInformation.Target.PRIVATE
    }

internal fun CouponInformation.Target?.convertToCouponTargetEnum() =
    if (this == CouponInformation.Target.PUBLIC) {
        CouponTargetEnum.PUBLIC
    } else {
        CouponTargetEnum.PRIVATE
    }