package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastApplyMessageInfoUiModel(
    var detail: String = "",
    var message: String = ""
) : Parcelable
