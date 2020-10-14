package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MvcShippingBenefitUiModel(
        var benefitAmount: Int = 0,
        var spId: Int = 0
) : Parcelable