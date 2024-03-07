package com.tokopedia.purchase_platform.common.feature.promo.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoExternalAutoApply(
    val code: String = "",
    val type: String = ""
) : Parcelable
