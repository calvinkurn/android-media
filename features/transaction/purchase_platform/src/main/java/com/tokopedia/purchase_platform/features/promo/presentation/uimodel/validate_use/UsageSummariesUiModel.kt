package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 16/03/20.
 */
@Parcelize
data class UsageSummariesUiModel (
        var desc: String = "",
        var type: String = "",
        var amountStr: String = "",
        var amount: Int = -1
): Parcelable