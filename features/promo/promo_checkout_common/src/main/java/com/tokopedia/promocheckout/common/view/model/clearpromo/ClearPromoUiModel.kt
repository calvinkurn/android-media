package com.tokopedia.promocheckout.common.view.model.clearpromo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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