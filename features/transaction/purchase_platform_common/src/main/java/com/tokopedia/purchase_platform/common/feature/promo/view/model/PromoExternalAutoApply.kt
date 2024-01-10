package com.tokopedia.purchase_platform.common.feature.promo.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoExternalAutoApply(
    var code: String = "",
    var type: String = ""
) : Parcelable
