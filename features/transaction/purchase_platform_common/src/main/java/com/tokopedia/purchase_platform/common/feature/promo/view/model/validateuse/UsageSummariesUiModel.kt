package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 16/03/20.
 */
@Parcelize
data class UsageSummariesUiModel(
        var desc: String = "",
        var type: String = "",
        var amountStr: String = "",
        var amount: Int = -1,
        var currencyDetailStr: String = ""
) : Parcelable