package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClashingInfoDetailUiModel(
    var isClashedPromos: Boolean = false,
    var options: List<PromoClashOptionUiModel> = emptyList(),
    var clashReason: String = "",
    var clashMessage: String = ""
) : Parcelable
