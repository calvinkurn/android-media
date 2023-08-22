package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastApplyBebasOngkirInfo(
    var isBoUnstackEnabled: Boolean = false,
    var isUseBebasOngkirOnly: Boolean = false
) : Parcelable
