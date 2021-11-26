package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackingDetailsItemUiModel(
        var promoDetailsTracking: String = "",
        var productId: Long = 0,
        var promoCodesTracking: String = ""
) : Parcelable