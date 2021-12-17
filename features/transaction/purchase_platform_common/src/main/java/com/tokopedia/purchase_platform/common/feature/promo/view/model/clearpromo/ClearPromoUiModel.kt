package com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClearPromoUiModel(
        var successDataModel: SuccessDataUiModel = SuccessDataUiModel()
) : Parcelable

@Parcelize
data class SuccessDataUiModel(
        var success: Boolean = false,
        var tickerMessage: String = "",
        var defaultEmptyPromoMessage: String = ""
) : Parcelable